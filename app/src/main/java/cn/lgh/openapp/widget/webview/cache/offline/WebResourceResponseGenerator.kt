package cn.lgh.openapp.widget.webview.cache.offline

import android.webkit.WebResourceResponse

/**
 * @author lgh
 * @date 2021/5/21
 *
 */
interface WebResourceResponseGenerator {
    fun generate(resource: WebResource?,urlMine:String?):WebResourceResponse?
}