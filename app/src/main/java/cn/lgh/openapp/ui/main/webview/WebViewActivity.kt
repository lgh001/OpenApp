package cn.lgh.openapp.ui.main.webview

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.webkit.WebView
import android.widget.LinearLayout
import cn.lgh.openapp.databinding.ActivityWebViewBinding
import cn.lgh.openapp.ui.base.BaseActivity
import cn.lgh.openapp.ui.base.BaseViewModel
import cn.lgh.openapp.widget.toast
import com.just.agentweb.AgentWeb
import com.just.agentweb.PermissionInterceptor
import com.just.agentweb.WebViewClient
import com.just.agentweb.WebChromeClient

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
    private var mUrl: String? = null
    private var mTitle: String? = null

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
        v.tvTitle.text = mTitle
    }

    override fun initVM() {}


    private val mWebViewClient=object : WebViewClient() {
        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
        }
    }

    private val mWebChromeClient=object :WebChromeClient(){
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
        }
    }

    private val mPermissionInterceptor=
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
        super.onResume()
    }

    override fun onPause() {
        mWebView?.get()?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onBackPressed() {
        if (mWebView?.get()?.back()==false){
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        mWebView?.get()?.webLifeCycle?.onDestroy()
        super.onDestroy()
    }
}