package cn.lgh.openapp.widget.webview.cache.offline

import okhttp3.internal.http.StatusLine
import java.net.HttpURLConnection

/**
 * @author lgh
 * @date 2021/5/20
 *
 */
class WebResource {

    var responseCode = 0

    var reasonPhrase: String? = null

    var responseHeaders: Map<String, String>? = null

    var isModified = true

    var isCacheByOurselves = false

    var originBytes: ByteArray? = null

    fun isCacheable(): Boolean {
        return responseCode == HttpURLConnection.HTTP_OK
                || responseCode == HttpURLConnection.HTTP_NOT_AUTHORITATIVE
                || responseCode == HttpURLConnection.HTTP_NO_CONTENT
                || responseCode == HttpURLConnection.HTTP_MULT_CHOICE
                || responseCode == HttpURLConnection.HTTP_MOVED_PERM
                || responseCode == HttpURLConnection.HTTP_NOT_FOUND
                || responseCode == HttpURLConnection.HTTP_BAD_METHOD
                || responseCode == HttpURLConnection.HTTP_GONE
                || responseCode == HttpURLConnection.HTTP_REQ_TOO_LONG
                || responseCode == HttpURLConnection.HTTP_NOT_IMPLEMENTED
                || responseCode == StatusLine.HTTP_PERM_REDIRECT
    }
}