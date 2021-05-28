package cn.lgh.openapp.widget.webview.cache.config

/**
 * @author lgh
 * @date 2021/5/20
 *
 */
interface MimeTypeFilter {

    fun isFilter(mimeType:String?):Boolean
    fun isImageFilter(mimeType: String?):Boolean
    fun addMimeType(mimeType: String)
    fun removeMimeType(mimeType: String)
    fun clear()
}