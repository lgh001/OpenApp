package cn.lgh.openapp.widget.webview.cache

import android.webkit.WebView

/**
 * @author lgh
 * @date 2021/5/28
 *
 */
interface IWebView {

    var hit:Boolean
    fun get(): WebView
    fun isHit(): Boolean
}