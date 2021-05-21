package cn.lgh.openapp.widget.webview.cache.offline

import android.util.LruCache
import cn.lgh.openapp.widget.webview.cache.config.CacheConfig

/**
 * @author lgh
 * @date 2021/5/20
 * 内存缓存
 */
class MemoryCacheInterceptor(config: CacheConfig) : ResourceInterceptor, Destroyable {

    private var mLruCache: MemLruCache? = null

    init {
        if (config.memCacheSize > 0) {
            mLruCache = MemLruCache(config.memCacheSize)
        }
    }

    override fun intercept(chain: ResourceInterceptor.Chain): WebResource? {
        val request = chain.request()
        if (mLruCache != null) {
            val resource = mLruCache?.get(request?.key)
            if (checkResourceValid(resource)) {
                return resource
            }
        }
        val webResource = chain.process(request)
        if (mLruCache != null && checkResourceValid(webResource) && webResource?.isCacheable() == true) {
            mLruCache?.put(request?.key, webResource)
        }
        return webResource
    }

    private fun checkResourceValid(resource: WebResource?): Boolean {
        return resource?.originBytes != null && resource.originBytes?.size!! > 0 && !resource.responseHeaders.isNullOrEmpty()
    }

    override fun destroy() {
        mLruCache?.evictAll()
        mLruCache = null
    }

    class MemLruCache(var maxSize: Int) : LruCache<String, WebResource>(maxSize) {

        override fun sizeOf(key: String?, value: WebResource?): Int {
            var size = 0
            value?.let {
                it.originBytes?.let { bytes ->
                    size = bytes.size
                }
            }
            return size
        }
    }
}