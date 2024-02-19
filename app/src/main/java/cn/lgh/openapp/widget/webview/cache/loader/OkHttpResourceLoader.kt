package cn.lgh.openapp.widget.webview.cache.loader

import cn.lgh.openapp.widget.webview.cache.offline.WebResource
import cn.lgh.openapp.widget.webview.cache.okhttp.OkHttpClientProvider
import cn.lgh.openapp.widget.webview.cache.utils.CacheLog
import cn.lgh.openapp.widget.webview.cache.utils.HeaderUtils
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.util.*
import kotlin.math.log

/**
 * @author lgh
 * @date 2021/5/21
 *
 */
class OkHttpResourceLoader() : ResourceLoader {

    companion object {
        private const val HEADER_USER_AGENT = "User-Agent"
        private const val DEFAULT_USER_AGENT = "WebView"
    }

    override fun getResource(request: SourceRequest?): WebResource? {
        val url = request?.url ?: return null
        if (!url.startsWith("http") || !url.startsWith("https")) return null
        val httpClient = OkHttpClientProvider.get()
        //CacheControl?
        val userAgent = request.userAgent ?: DEFAULT_USER_AGENT
        val acceptLanguage = Locale.getDefault().language
        val requestBuilder = Request.Builder()
            .removeHeader(HEADER_USER_AGENT)
            .addHeader(HEADER_USER_AGENT, userAgent)
            .addHeader("Upgrade-Insecure-Requests", "1")
            .addHeader("Accept", "*/*")
            .addHeader("Accept-Language", acceptLanguage)
        request.headers?.let {
            for (entry in it) {
                val key = entry.key
                if (!isNeedStripHeader(key)) {
                    requestBuilder.removeHeader(key)
                    requestBuilder.addHeader(key, entry.value)
                }
            }
        }

        var response: Response? = null
        try {
            val httpRequest = requestBuilder
                .url(url)
                .get().build()
            val webResource = WebResource()
            response = httpClient.newCall(httpRequest).execute()
            CacheLog.d("okhttp: ${response.code}   $url  ${response.headers["Content-Type"]}")
            if (isInterceptorThisRequest(response)) {
                webResource.responseCode = response.code
                webResource.reasonPhrase = response.message
                webResource.isModified = response.code != HttpURLConnection.HTTP_NOT_MODIFIED
                response.body?.let {
                    webResource.originBytes = it.bytes()
                }
                webResource.responseHeaders = HeaderUtils.generateHeadersMap(response.headers)
                webResource.isCacheByOurselves = !request.cacheable
                return webResource
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            response?.close()
        }
        return null
    }

    /**
     * 需要剥离的header
     * @param name String
     * @return Boolean
     */
    private fun isNeedStripHeader(name: String): Boolean {
        return name.equals("If-Match", true)
                || name.equals("If-None-Match", true)
                || name.equals("If-Modified-Since", true)
                || name.equals("If-Unmodified-Since", true)
                || name.equals("Last-Modified", true)
                || name.equals("Expires", true)
                || name.equals("Cache-Control", true)
    }

    /**
     * 如果 <100 >600的忽略   300-400之间的也忽略(300-400是重定向)
     * @param response Response
     * @return Boolean
     */
    private fun isInterceptorThisRequest(response: Response): Boolean {
        val code = response.code
        return !(code < 100 || code > 599 || code in 300..399)
    }
}