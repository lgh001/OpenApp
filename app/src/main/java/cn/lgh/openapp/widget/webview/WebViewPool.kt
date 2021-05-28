package cn.lgh.openapp.widget.webview

import android.content.Context
import android.os.Build
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import cn.lgh.openapp.widget.webview.cache.IWebView

/**
 * @author lgh
 * @date 2021/5/18
 *
 */
class WebViewPool<K : String, V : IWebView>(
    val context: Context,
    preLoad: Array<String>? = arrayOf()
) {

    companion object {
        private const val DEFAULT_CACHE_COUNT = 4
        private const val TAG = "WebViewPool"
        const val DEF_WEB_VIEW_URL = "about:blank"
    }

    var size = 0
        private set
    private var mLastNode: Node<String, V>? = null

    init {
        var size = if (preLoad?.size ?: 0 > 3) 3 else preLoad?.size ?: 0
        var i = 0
        while (i < size) {
            add(preLoad!![i], initWebView(context, preLoad[i]))
            i++
        }
        while (i < DEFAULT_CACHE_COUNT) {
            add(DEF_WEB_VIEW_URL, initWebView(context, null))
            i++
        }

    }

    private fun initWebView(context: Context, url: String?): V {
        val webView = WebViewImpl(context)
        val webSettings = webView.settings;
        webSettings.useWideViewPort = true; // 可任意比例缩放
        // 设置支持js
        webSettings.setJavaScriptEnabled(true)
        // 设置可以访问文件
        webSettings.allowFileAccess = true;
        webSettings.allowFileAccessFromFileURLs = false;
        webSettings.allowUniversalAccessFromFileURLs = false;
        webSettings.allowContentAccess = true;
        webSettings.displayZoomControls = true;
        //
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
//        webSettings.setAppCacheEnabled(true)
        // 开启数据库形式存储
        webSettings.databaseEnabled = true
        // 开启DOM形式存储
        webSettings.domStorageEnabled = true
        // 支持自动加载图片
        webSettings.loadsImagesAutomatically = true
        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW;
        }
        webView.loadUrl(url ?: DEF_WEB_VIEW_URL)
        return webView as V
    }

    /**
     * 添加一个节点
     * @param key K
     * @param value V
     */
    private fun add(key: String, value: V) {
        val newNode = Node(key, value, mLastNode, null)
        if (mLastNode != null) {
            mLastNode?.next = newNode
        }
        mLastNode = newNode
        size++
    }

    /**
     * 移除最后的节点
     * @return Boolean true表示移除成功
     */
    private fun removeLast(): Boolean {
        if (mLastNode == null) {
            return false
        }
        val prev = mLastNode?.prev
        mLastNode?.prev = null
        mLastNode = null
        if (prev != null) {
            prev.next == null
            mLastNode = prev
        }
        size--
        return true
    }

    /**
     * 移除所有节点
     * @return Boolean
     */
    private fun removeAll(): Boolean {
        while (removeLast()) {
        }
        return true
    }

    fun show() {
        if (mLastNode == null) {
            Log.e(TAG, "WebViewPoll == null")
            return
        }
        var current = mLastNode
        while (true) {
            val prev = current?.prev
            if (prev == null) {
                break
            } else {
                current = prev
            }
        }
    }

    /**
     *
     * 返回某个节点
     *
     * 从末尾开始查找，如果找不到则返回最前面的节点
     * @param key K
     * @return V
     */
    fun get(key: String?): V? {
        if (mLastNode == null) return null
        var current = mLastNode
        var hit = false
        while (true) {
            val prev = current?.prev
            if (prev == null) {
                changeNodeToLast(current)
                current?.key = key
                current?.value?.hit = false
                break
            } else if (current?.key == key) {
                changeNodeToLast(current)
                current?.value?.hit = true
                hit = true
                break
            } else {
                current = prev
            }
        }
        //如果未命中，需要将头结点变成空，保证池里永远有一个空的webview
        if (!hit) {
            letHeadEmpty()
        }
        return current?.value
    }

    /**
     * 让头结点变为空
     */
    private fun letHeadEmpty() {
        if (mLastNode == null) return
        var cur = mLastNode
        while (cur?.prev != null) {
            cur = cur.prev
        }
        var head = cur
        if (head?.key != DEF_WEB_VIEW_URL) {
            head?.key = DEF_WEB_VIEW_URL as String
            head?.value?.get()?.webViewClient = WebViewClient()
            head?.value?.get()?.webChromeClient = null
            head?.value?.get()?.loadUrl(DEF_WEB_VIEW_URL)
        }
    }

    /**
     * 将节点移动到末尾
     * @param target Node<K, V> 需要移动到末尾的节点
     */
    private fun changeNodeToLast(target: Node<String, V>?) {
        //如果是末尾节点，不需要移动
        val next = target?.next ?: return

        val prev = target.prev
        //如果节点的前驱节点为空，意味着节点在最前面
        if (prev == null) {
            //把当前节点的后继节点的前驱设置为空，主要目的是为了断开链表，方便移动到末尾
            next.prev = null
        } else {
            //否则，节点在中间
            //  a <--> current <--> b
            //=>  a <--> b
            //下面代码完成上面的操作
            prev.next = next
            next.prev = prev
        }
        //将当前节点放到最后节点的后面
        target.next = null
        target.prev = mLastNode
        mLastNode?.next = target
        mLastNode = target
    }


    private class Node<K, V>(
        var key: K?,
        var value: V?,
        var prev: Node<K, V>?,
        var next: Node<K, V>?
    )
}