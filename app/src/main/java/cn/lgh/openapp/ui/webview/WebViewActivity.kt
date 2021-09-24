package cn.lgh.openapp.ui.webview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.widget.LinearLayout
import cn.lgh.openapp.databinding.ActivityWebViewBinding
import cn.lgh.openapp.ui.base.BaseActivity
import cn.lgh.openapp.ui.base.BaseViewModel
import cn.lgh.openapp.widget.toast
import cn.lgh.openapp.widget.webview.cache.WebViewClientDelegate
import com.google.gson.Gson
import com.just.agentweb.AgentWeb
import com.just.agentweb.PermissionInterceptor
import com.just.agentweb.WebChromeClient
import com.just.agentweb.WebViewClient

/**
 * @author lgh
 * @date 2020/12/16
 * webView
 */
class WebViewActivity : BaseActivity<BaseViewModel, ActivityWebViewBinding>() {

    companion object {

        const val PARAM_KEY_URL = "PARAM_KEY_URL"
        const val PARAM_KEY_TITLE = "PARAM_KEY_TITLE"

        /**
         * 打开WebView
         * @param context Context? 上下文
         * @param url String url
         * @param title String? 标题
         */
        @JvmStatic
        fun start(context: Context?, url: String, title: String? = "") {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(PARAM_KEY_URL, url)
            intent.putExtra(PARAM_KEY_TITLE, title)
            context?.startActivity(intent)
        }
    }

        private var mWebView: AgentWeb.PreAgentWeb? = null
    private var webView: WebView? = null
    private var mUrl: String? = null
    private var mTitle: String? = null
    private var mWebViewClientDelegate: WebViewClientDelegate? = null

    override fun initView() {
        mWebView = AgentWeb.with(this)
            .setAgentWebParent(v.root, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebChromeClient(mWebChromeClient)
            .setWebViewClient(mWebViewClient)
            .setPermissionInterceptor(mPermissionInterceptor)
            .createAgentWeb()
            .ready()

        //android 调用js
//        mWebView?.get()?.jsAccessEntrace.quickCallJs("callByAndroid")
        //js 调用android
//        mWebView?.get()?.let {
//            it.jsInterfaceHolder?.addJavaObject("android",AndroidInterface(it,this))
//        }
    }

    override fun initListener() {
        v.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initData(bundle: Bundle?) {
        mUrl = bundle?.getString(PARAM_KEY_URL) ?: intent?.getStringExtra(PARAM_KEY_URL)
        mTitle = bundle?.getString(PARAM_KEY_TITLE) ?: intent?.getStringExtra(PARAM_KEY_TITLE)
        if (mUrl == null) {
            toast("url 不能为空")
            finish()
            return
        }
        mWebView?.go(mUrl)
//        webView = WebViewTools.instance.get(mUrl)?.get()
//        val webSettings = webView!!.settings
//        webSettings.setJavaScriptEnabled(true)
//        webSettings.domStorageEnabled = true
//        webSettings.allowFileAccess = true
//        webSettings.useWideViewPort = true
//        webSettings.loadWithOverviewMode = true
//        webSettings.setSupportZoom(false)
//        webSettings.builtInZoomControls = false
//        webSettings.displayZoomControls = false
//        webSettings.defaultTextEncodingName = "UTF-8"
//        webSettings.blockNetworkImage = true
//        webView?.addJavascriptInterface(this, "android")
//        initWebView()
        v.tvTitle.text = mTitle
    }

    private fun initWebView() {
        mWebViewClientDelegate = WebViewClientDelegate(webView!!, mWebViewClient)
        webView?.apply {
            webChromeClient = mWebChromeClient
            webViewClient = mWebViewClientDelegate!!
        }
        webView?.let {
            v.root.addView(it)
        }
        webView?.loadUrl(mUrl!!)
        time = System.currentTimeMillis()
    }

    private var time = 0L
    private var time1 = 0L

    override fun initVM() {}

    @JavascriptInterface
    fun sendResource(timing: String?) {
        val performance: Performance = Gson().fromJson(timing, Performance::class.java)
        Log.v(
            "WebViewActivity",
            "request cost time: " + (performance.responseEnd - performance.requestStart).toString() + "ms"
        )
        Log.v(
            "WebViewActivity",
            "dom build time: " + (performance.domComplete - performance.domInteractive).toString() + "ms."
        )
        Log.v(
            "WebViewActivity",
            "dom ready time: " + (performance.domContentLoadedEventEnd - performance.navigationStart).toString() + "ms."
        )
        Log.v(
            "WebViewActivity",
            "load time: " + (performance.loadEventEnd - performance.navigationStart).toString() + "ms."
        )
    }

    private val mWebViewClient = object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
//            time1 = System.currentTimeMillis()
//            println("onPageStarted：$url")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
//            view!!.settings.blockNetworkImage = false
//            view?.loadUrl("javascript:android.sendResource(JSON.stringify(window.performance.timing))")
//            println("start: ${time1 - time}")
//            println("load: ${System.currentTimeMillis() - time1}")
        }
    }

    private val mWebChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }
    }

    private val mPermissionInterceptor =
        PermissionInterceptor { url, permissions, action ->
            Log.i("WebView", "url: $url   permissions: $permissions    action: $action")
            false
        }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mWebView?.get()?.handleKeyEvent(keyCode,event) == true){
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        mWebView?.get()?.webLifeCycle?.onResume()
//        webView?.onResume()
        super.onResume()
    }

    override fun onPause() {
        mWebView?.get()?.webLifeCycle?.onPause()
//        webView?.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        if (mWebView?.get()?.back()==false){
            super.onBackPressed()
        }
        super.onBackPressed()
    }

    override fun onDestroy() {
        mWebView?.get()?.webLifeCycle?.onDestroy()
//        WebViewTools.instance.remove(v.root, webView)
        super.onDestroy()
    }
}