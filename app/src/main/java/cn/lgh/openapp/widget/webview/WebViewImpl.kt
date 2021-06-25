package cn.lgh.openapp.widget.webview

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView
import cn.lgh.openapp.widget.webview.cache.IWebView

/**
 * @author lgh
 * @date 2021/5/28
 *
 */
class WebViewImpl : WebView, IWebView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override var hit: Boolean = false
    override var key: String =""

    override fun get(): WebView = this

    override fun isHit(): Boolean = hit

    override fun loadUrl(url: String) {
        if (!hit) {
            super.loadUrl(url)
        }else{
            settings.blockNetworkImage = false
        }
        hit = false
    }
}