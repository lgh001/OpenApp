package cn.lgh.openapp.widget.webview.cache.offline

import cn.lgh.openapp.widget.webview.cache.utils.MD5Utils
import java.net.URLEncoder

/**
 * @author lgh
 * @date 2021/5/20
 *
 */
class CacheRequest {

    var key: String? = null
        private set
    var url: String? = null
        set(value) {
            field=value
            key=generateKey(value)
        }
    var mime: String? = null
    var forceMode = false
    var mHeaders: Map<String, String>? = null
    var mUserAgent: String? = null
    var mWebViewCacheMode = 0

    private fun generateKey(url:String?):String?{
        return MD5Utils.getMD5(URLEncoder.encode(url,"utf-8"),false)
    }
}