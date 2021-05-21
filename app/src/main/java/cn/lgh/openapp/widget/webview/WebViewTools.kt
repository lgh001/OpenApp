package cn.lgh.openapp.widget.webview

import android.content.Context
import android.content.Intent
import android.view.ViewGroup
import android.webkit.WebView

/**
 * @author lgh
 * @date 2021/5/19
 *
 */
class WebViewTools private constructor() {

    private object SingletonHolder {
        val holder = WebViewTools()
    }

    companion object {
        val instance = SingletonHolder.holder
        private const val ACTION_START_WEB = "spt.webview.web.start.action"
        private const val ACTION_STOP_WEB = "spt.webview.web.stop.action"

        fun initialize(context: Context) {
            val processName = ProcessUtils.getAppProcess(context)
            println("进程名：$processName")
            if ("cn.lgh.openapp:web" == processName) {
                instance.init(context)
            } else {
                instance.startProcess(context)
            }
        }
    }

    private var mPool: WebViewPool<String, WebView>? = null

    fun init(context: Context) {
        println("初始化")
        mPool = WebViewPool(context)
    }

    fun startProcess(context: Context) {
        val intent = Intent(context,WebStartService::class.java)
        context.startService(intent)
    }

    fun stopProcess(context: Context) {
        val intent = Intent(context,WebStartService::class.java)
        context.startService(intent)
    }

    /**
     * 根据要加载的页面url，从缓存池中获取webview,该方法遵循最近访问原则
     * 如果没找到当前url对应的地址，默认返回最前面的webview
     * @param url String
     * @return WebView?
     */
    fun get(url: String?): WebView? {
        return mPool?.get(url)
    }

    fun remove(webView: WebView?) {
        val viewGroup = webView?.parent as ViewGroup
        viewGroup?.removeView(webView)
        webView?.removeAllViews()
        webView?.webChromeClient = null
//        webView.webViewClient = null
    }

    fun remove(viewGroup: ViewGroup, webView: WebView?) {
        viewGroup.removeView(webView)
        webView?.removeAllViews()
        webView?.webChromeClient = null
    }

}