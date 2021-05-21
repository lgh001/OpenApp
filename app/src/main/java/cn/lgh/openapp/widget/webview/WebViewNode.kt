package cn.lgh.openapp.widget.webview

import android.webkit.WebView

/**
 * @author lgh
 * @date 2021/5/19
 *
 */
class WebViewNode<T : WebView>(private val previous: WebViewNode<T>?, private val item: T) {

    fun hasPrevious(): Boolean {
        return previous != null
    }

    fun getSize(): Int {
        var size = 0
        if (hasPrevious()) {
            val node = previous!!
            do {
                size++
            } while (node.hasPrevious())
        }
        return size
    }

}