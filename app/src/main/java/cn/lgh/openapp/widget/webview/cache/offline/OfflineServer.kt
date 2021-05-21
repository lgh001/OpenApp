package cn.lgh.openapp.widget.webview.cache.offline

import android.webkit.WebResourceResponse

/**
 * @author lgh
 * @date 2021/5/21
 *
 */
interface OfflineServer {
    fun get(request: CacheRequest):WebResourceResponse?

    fun addResourceInterceptor(interceptor: ResourceInterceptor)
}