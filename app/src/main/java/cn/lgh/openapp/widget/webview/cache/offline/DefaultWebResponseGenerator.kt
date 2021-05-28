package cn.lgh.openapp.widget.webview.cache.offline

import android.text.TextUtils
import android.webkit.WebResourceResponse
import cn.lgh.openapp.widget.webview.cache.utils.CacheLog

/**
 * @author lgh
 * @date 2021/5/21
 *
 */
class DefaultWebResponseGenerator : WebResourceResponseGenerator {

    private val KEY_CONTENT_TYPE = "Content-Type"

    override fun generate(resource: WebResource?, urlMine: String?): WebResourceResponse? {
        if (resource == null) return null
        var mime = urlMine
        val headers = resource.responseHeaders
        var contentType: String? = null
        var charset: String? = null
        if (headers != null) {
            val contentTypeValue = getContentType(headers, KEY_CONTENT_TYPE)
            if (!TextUtils.isEmpty(contentTypeValue)) {
                val contentTypeArray =
                    contentTypeValue!!.split(";").toTypedArray()
                if (contentTypeArray.isNotEmpty()) {
                    contentType = contentTypeArray[0]
                }
                if (contentTypeArray.size >= 2) {
                    charset = contentTypeArray[1]
                    val charsetArray =
                        charset.split("=").toTypedArray()
                    if (charsetArray.size >= 2) {
                        charset = charsetArray[1]
                    }
                }
            }
        }
        if (!TextUtils.isEmpty(contentType)) {
            mime = contentType
        }
        if (TextUtils.isEmpty(mime)) {
            return null
        }
        resource.originBytes ?: return null
        if (resource.originBytes!!.isEmpty() && resource.responseCode == 304) {
            CacheLog.d("the response bytes can not be empty if we get 304.")
            return null
        }
        val bis = resource.originBytes?.inputStream()
        val status = resource.responseCode
        val reasonPhrase = if(resource.reasonPhrase.isNullOrEmpty()) PhraseList.getPhrase(status) else resource.reasonPhrase
        return WebResourceResponse(mime, charset, status, reasonPhrase?:PhraseList.getPhrase(status), headers, bis)
    }

    private fun getContentType(
        headers: Map<String, String>?,
        key: String
    ): String? {
        if (headers != null) {
            val value = headers[key]
            return value ?: headers[key.toLowerCase()]
        }
        return null
    }
}