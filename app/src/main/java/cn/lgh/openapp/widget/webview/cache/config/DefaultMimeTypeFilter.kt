package cn.lgh.openapp.widget.webview.cache.config

/**
 * @author lgh
 * @date 2021/5/21
 *
 */
class DefaultMimeTypeFilter : MimeTypeFilter {

    private val mFilterMimeTypes = hashSetOf<String>()
    private val mImageMimeTypes = hashSetOf<String>()

    init {
        // JavaScript
        addMimeType("application/javascript")
        addMimeType("application/ecmascript")
        addMimeType("application/x-ecmascript")
        addMimeType("application/x-javascript")
        addMimeType("application/x-font-ttf")
        addMimeType("text/ecmascript")
        addMimeType("text/javascript")
        addMimeType("text/javascript1.0")
        addMimeType("text/javascript1.1")
        addMimeType("text/javascript1.2")
        addMimeType("text/javascript1.3")
        addMimeType("text/javascript1.4")
        addMimeType("text/javascript1.5")
        addMimeType("text/jscript")
        addMimeType("text/livescript")
        addMimeType("text/x-ecmascript")
        addMimeType("text/x-javascript")
        addMimeType("text/plain")
        // image
//        addMimeType("image/gif")
//        addMimeType("image/jpeg")
//        addMimeType("image/png")
//        addMimeType("image/svg+xml")
//        addMimeType("image/bmp")
//        addMimeType("image/webp")
//        addMimeType("image/tiff")
//        addMimeType("image/vnd.microsoft.icon")
//        addMimeType("image/x-icon")
        // css
        addMimeType("text/css")
        // stream
        addMimeType("application/octet-stream")
//        addMimeType("text/html")

        mImageMimeTypes.add("image/gif")
        mImageMimeTypes.add("image/jpeg")
        mImageMimeTypes.add("image/png")
        mImageMimeTypes.add("image/svg+xml")
        mImageMimeTypes.add("image/bmp")
        mImageMimeTypes.add("image/webp")
        mImageMimeTypes.add("image/tiff")
        mImageMimeTypes.add("image/vnd.microsoft.icon")
        mImageMimeTypes.add("image/x-icon")
    }

    override fun isFilter(mimeType: String?): Boolean = !mFilterMimeTypes.contains(mimeType)
    override fun isImageFilter(mimeType: String?): Boolean = mImageMimeTypes.contains(mimeType)

    override fun addMimeType(mimeType: String) {
        mFilterMimeTypes.add(mimeType)
    }

    override fun removeMimeType(mimeType: String) {
        mFilterMimeTypes.remove(mimeType)
    }

    override fun clear() {
        mFilterMimeTypes.clear()
    }
}