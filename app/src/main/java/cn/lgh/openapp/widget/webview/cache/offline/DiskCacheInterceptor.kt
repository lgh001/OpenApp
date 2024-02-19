package cn.lgh.openapp.widget.webview.cache.offline

import android.text.TextUtils
import cn.lgh.openapp.widget.webview.cache.config.CacheConfig
import cn.lgh.openapp.widget.webview.cache.lru.DiskLruCache
import cn.lgh.openapp.widget.webview.cache.utils.CacheLog
import cn.lgh.openapp.widget.webview.cache.utils.HeaderUtils
import okhttp3.Headers
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.IOException
import java.util.*

/**
 * @author lgh
 * @date 2021/5/20
 *
 */
class DiskCacheInterceptor(val config: CacheConfig) : ResourceInterceptor, Destroyable {
    companion object {
        private const val ENTRY_META = 0
        private const val ENTRY_BODY = 1
        private const val ENTRY_COUNT = 2
    }

    private var mDiskLruCache: DiskLruCache? = null

    private fun ensureDiskLruCacheCreated() {
        if (mDiskLruCache != null && mDiskLruCache?.isClosed == false) return

        try {
            mDiskLruCache = DiskLruCache.open(
                config.cacheDir?.let { File(it) },
                config.version,
                ENTRY_COUNT,
                config.diskCacheSize
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun intercept(chain: ResourceInterceptor.Chain): WebResource? {
        val request = chain.request()
        ensureDiskLruCacheCreated()
        val diskResource = getFromDiskCache(request?.key)
        if (isRealMimeTypeCacheable(diskResource)) {
            CacheLog.d("disk cache hit: ${request?.url}")
            return diskResource
        }
        val webResource = chain.process(request)
        if (isRealMimeTypeCacheable(webResource)) {
            saveToDisk(request?.key, webResource)
        }
        return webResource
    }

    private fun getFromDiskCache(key: String?): WebResource? {
        if (mDiskLruCache?.isClosed == true) return null
        val snapstot = mDiskLruCache?.get(key)
        return snapstot?.let {
            val buffer = it.getInputStream(ENTRY_META).source().buffer()
            //1.read status
            val responseCode = buffer.readUtf8LineStrict()
            val reasonPhrase = buffer.readUtf8LineStrict()
            //2.read header
            var headerSize = buffer.readDecimalLong()
            val headers: Map<String, String>
            val builder = Headers.Builder()
            val placeHolder = buffer.readUtf8LineStrict()
            if (placeHolder.trim().isNotEmpty()) {
                builder.add(placeHolder)
                headerSize--
            }
            for (i in 0 until headerSize) {
                val line = buffer.readUtf8LineStrict()
                if (line.isNotEmpty()) {
                    builder.add(line)
                }
            }
            headers = HeaderUtils.generateHeadersMap(builder.build())
            //3.read body
            val bodyOs = snapstot.getInputStream(ENTRY_BODY)
            if (bodyOs != null) {
                val resource = WebResource()
                resource.responseCode = responseCode.toInt()
                resource.reasonPhrase = reasonPhrase
                resource.responseHeaders = headers
                resource.originBytes = bodyOs.readBytes()
                resource.isModified = false
                it.close()
                resource
            } else {
                null
            }
        }
    }

    private fun saveToDisk(key: String?, resource: WebResource?) {
        if (resource?.isCacheable() == false) return
        if (mDiskLruCache?.isClosed == true) return
        try {
            val editor = mDiskLruCache?.edit(key)
            if (editor == null) {
                CacheLog.e("Another edit is in progress!")
                return
            }
            val os = editor.newOutputStream(ENTRY_META)
            var sink = os.sink().buffer()
            //1.写入 status
            sink.writeUtf8("${resource?.responseCode}").writeByte('\n'.code)
            sink.writeUtf8("${resource?.reasonPhrase}").writeByte('\n'.code)
            //2.写入 response header
            val headers = resource?.responseHeaders
            sink.writeDecimalLong(headers?.size?.toLong() ?: 0L).writeByte('\n'.code)
            headers?.let {
                for (entry in it.entries) {
                    sink.writeUtf8(entry.key)
                        .writeUtf8(":")
                        .writeUtf8(entry.value)
                        .writeByte('\n'.code)
                }
            }
            sink.flush()
            sink.close()
            //3.写入 body
            val bodyOs = editor.newOutputStream(ENTRY_BODY)
            sink = bodyOs.sink().buffer()
            resource?.originBytes?.let {
                if (it.isNotEmpty()) {
                    sink.write(it)
                    sink.flush()
                    editor.commit()
                }
            }
            sink.close()
        } catch (e: IOException) {
            CacheLog.e("cache to disk failed. cause by: ${e.message}")
            try {
                mDiskLruCache?.remove(key)
            } catch (ignore: Exception) {
            }
        } catch (e: Exception) {
            CacheLog.e(e.message)
        }
    }

    private fun isRealMimeTypeCacheable(resource: WebResource?): Boolean {
        if (resource == null) {
            return false
        }
        val headers: Map<String, String>? = resource.responseHeaders
        var contentType: String? = null
        if (headers != null) {
            val uppercaseKey = "Content-Type"
            val lowercaseKey = uppercaseKey.lowercase(Locale.getDefault())
            val contentTypeValue =
                if (headers.containsKey(uppercaseKey)) headers[uppercaseKey] else headers[lowercaseKey]
            if (!TextUtils.isEmpty(contentTypeValue)) {
                val contentTypeArray =
                    contentTypeValue?.split(";")?.toTypedArray()
                if (!contentTypeArray.isNullOrEmpty()) {
                    contentType = contentTypeArray[0]
                }
            }
        }
        val code=resource.responseCode
        return contentType != null && config.mimeTypeFilter?.isFilter(contentType) == false && code in 200..299
    }

    override fun destroy() {
        if (mDiskLruCache?.isClosed == false) {
            mDiskLruCache?.close()
        }
    }
}