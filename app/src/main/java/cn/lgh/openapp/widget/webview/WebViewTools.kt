package cn.lgh.openapp.widget.webview

import android.content.Context
import android.content.Intent
import android.content.MutableContextWrapper
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import cn.lgh.openapp.widget.webview.cache.IWebView

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

        fun initialize(context: Context, preLoad: Array<String>? = arrayOf()) {
            val processName = ProcessUtils.getAppProcess(context)
            val packageName=context.applicationInfo.packageName
            println("进程名：$processName  $packageName")
            if ("${packageName}:web" == processName) {
                instance.init(context, preLoad)
            } else {
                instance.startProcess(context)
            }
        }
    }

    private var mPool: WebViewPool<IWebView>? = null

    fun init(context: Context, preLoad: Array<String>? = arrayOf()) {
        println("初始化")
        mPool = WebViewPool(context, preLoad)
    }

    fun startProcess(context: Context) {
        val intent = Intent(context, WebStartService::class.java)
        context.startService(intent)
    }

    fun stopProcess(context: Context) {
        val intent = Intent(context, WebStartService::class.java)
        context.stopService(intent)
    }

    /**
     * 根据要加载的页面url，从缓存池中获取webview,该方法遵循最近访问原则
     * 如果没找到当前url对应的地址，默认返回最前面的webview
     * @param url String
     * @return WebView?
     */
    fun get(context: Context, url: String?): IWebView? {
        val view=mPool?.get(url)
        if (view?.get() != null) {
            val contextWrapper = view.get().context as MutableContextWrapper
            contextWrapper.baseContext = context
        }
        return view
    }

    fun release(webView: IWebView?) {
        if (webView?.get() == null) return
        val contextWrapper = webView.get().context as MutableContextWrapper
        contextWrapper.baseContext = contextWrapper.applicationContext
        remove(webView)
//        if (webView.isRelease) {
//            mPool?.addDefault()
//        } else {
//            mPool?.release(webView)
//        }
        mPool?.release(webView)
    }

    fun remove(webView: IWebView?) {
        val viewGroup = webView?.get()?.parent as ViewGroup
        viewGroup.removeView(webView.get())
        webView.get().removeAllViews()
        webView.get().webChromeClient = null
//        webView?.get().webChromeClient = null
    }

    fun remove(viewGroup: ViewGroup, webView: WebView?) {
        viewGroup.removeView(webView)
        webView?.removeAllViews()
        webView?.webChromeClient = null
        webView?.webViewClient= WebViewClient()
    }

}