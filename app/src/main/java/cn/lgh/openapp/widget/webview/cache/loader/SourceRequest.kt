package cn.lgh.openapp.widget.webview.cache.loader

import cn.lgh.openapp.widget.webview.cache.offline.CacheRequest

/**
 * @author lgh
 * @date 2021/5/21
 *
 */
class SourceRequest {
    var url: kotlin.String? = null
    var cacheable = false
    var headers: Map<String, String>? = null
    var userAgent: String? = null
    var webViewCache = 0

    constructor(request: CacheRequest?, cacheable: Boolean) {
        this.url = request?.url
        this.headers = request?.mHeaders
        this.userAgent = request?.mUserAgent
        this.webViewCache = request?.mWebViewCacheMode ?: 0
        this.cacheable = cacheable
    }
}