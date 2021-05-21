package cn.lgh.openapp.widget.webview

import android.content.Context
import android.os.Build
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * @author lgh
 * @date 2021/5/18
 *
 */
class WebViewPool<K : String, V : WebView>(val context: Context) {

    companion object {
        private const val DEFAULT_CACHE_COUNT = 3
        private const val TAG = "WebViewPool"
        private const val DEF_WEB_VIEW_URL = "about:blank"
    }

    var size = 0
        private set
    private var mLastNode: Node<K, V>? = null

    init {
        var i = 0
        while (i < DEFAULT_CACHE_COUNT) {
            add(DEF_WEB_VIEW_URL as K,initWebView(context))
            i++
        }

    }

    private fun initWebView(context: Context): V {
        println("webview")
        val webView = WebView(context)
        val webSettings = webView.settings;
        webSettings.useWideViewPort = true; // 可任意比例缩放
        // 设置支持js
        webSettings.setJavaScriptEnabled(true);
        // 设置渲染优先级
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        // 设置可以访问文件
        webSettings.allowFileAccess = true;
        webSettings.allowFileAccessFromFileURLs = false;
        webSettings.allowUniversalAccessFromFileURLs = false;
        webSettings.allowContentAccess = true;
        webSettings.displayZoomControls = true;
        //
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT;
        webSettings.setAppCacheEnabled(true);
        // 开启数据库形式存储
        webSettings.databaseEnabled = true;
        // 开启DOM形式存储
        webSettings.domStorageEnabled = true;
        // 支持自动加载图片
        webSettings.loadsImagesAutomatically = true;
        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW;
        }
        webView.loadUrl(DEF_WEB_VIEW_URL)
        return webView as V
    }

    /**
     * 添加一个节点
     * @param key K
     * @param value V
     */
    private fun add(key: K, value: V) {
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
    fun get(key: K?): V? {
        if (mLastNode == null) return null
        var current = mLastNode
        while (true) {
            val prev = current?.prev
            if (prev == null) {
                changeNodeToLast(current)
                current?.key = key
                return current?.value
            } else if (current?.key == key) {
                changeNodeToLast(current)
                return current?.value
            } else {
                current = prev
            }
        }
    }

    /**
     * 将节点移动到末尾
     * @param target Node<K, V> 需要移动到末尾的节点
     */
    private fun changeNodeToLast(target: Node<K, V>?) {
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