package cn.lgh.openapp.widget.webview.cache

import android.graphics.Bitmap
import android.os.Build
import android.os.Message
import android.view.KeyEvent
import android.webkit.*
import androidx.annotation.RequiresApi
import cn.lgh.openapp.widget.webview.cache.config.CacheConfig
import cn.lgh.openapp.widget.webview.cache.offline.CacheRequest
import cn.lgh.openapp.widget.webview.cache.offline.OfflineServer
import cn.lgh.openapp.widget.webview.cache.offline.OfflineServerImpl
import cn.lgh.openapp.widget.webview.cache.utils.MimeTypeMapUtils

/**
 * @author lgh
 * @date 2021/5/26
 *
 */
class WebViewClientDelegate(
    private val owner: WebView,
    private val delegate: WebViewClient? = null
) : WebViewClient() {

    private var offlineServer: OfflineServer? = null
    init {
        val config = CacheConfig.Builder(owner.context)
            .build()
        offlineServer = OfflineServerImpl(owner.context, config)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        if (delegate != null) {
            delegate.onPageFinished(view, url)
            return
        }
        super.onPageFinished(view, url)
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?): WebResourceResponse? {
        if (delegate != null) {
            return delegate.shouldInterceptRequest(view, url)
        }
        return super.shouldInterceptRequest(view, url)
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        val url = request?.url.toString()
        val extension = MimeTypeMapUtils.getFileExtensionFromUrl(url)
        val mimeType = MimeTypeMapUtils.getMimeTypeFromExtension(extension)
        val cacheRequest = CacheRequest()
        cacheRequest.url = url
        cacheRequest.mime = mimeType
        cacheRequest.mHeaders = request?.requestHeaders
        cacheRequest.mUserAgent = "OpenApp"
        val res = getOfflineServer()?.get(cacheRequest)
        if (res != null) {
            return res
        }
        return super.shouldInterceptRequest(view, request)
    }

    private fun getOfflineServer(): OfflineServer? {
        return offlineServer
    }

    override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
        if (delegate != null) {
            return delegate.shouldOverrideKeyEvent(view, event)
        }
        return super.shouldOverrideKeyEvent(view, event)
    }

    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        if (delegate != null) {
            delegate.onReceivedError(view, errorCode, description, failingUrl)
            return
        }
        super.onReceivedError(view, errorCode, description, failingUrl)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        if (delegate != null) {
            delegate.onReceivedError(view, request, error)
            return
        }
        super.onReceivedError(view, request, error)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        if (delegate != null) {
            delegate.onPageStarted(view, url, favicon)
            return
        }
        super.onPageStarted(view, url, favicon)
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        if (delegate != null) {
            return delegate.shouldOverrideUrlLoading(view, url)
        }
        return super.shouldOverrideUrlLoading(view, url)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        if (delegate != null) {
            return delegate.shouldOverrideUrlLoading(view, request)
        }
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
        if (delegate != null) {
            delegate.onReceivedClientCertRequest(view, request)
            return
        }
        super.onReceivedClientCertRequest(view, request)
    }

    override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
        if (delegate != null) {
            delegate.onFormResubmission(view, dontResend, resend)
            return
        }
        super.onFormResubmission(view, dontResend, resend)
    }

    override fun onLoadResource(view: WebView?, url: String?) {
        if (delegate != null) {
            delegate.onLoadResource(view, url)
            return
        }
        super.onLoadResource(view, url)
    }
}