package cn.lgh.openapp.widget.webview.cache.offline

import android.content.Context
import cn.lgh.openapp.widget.webview.cache.config.CacheConfig
import cn.lgh.openapp.widget.webview.cache.config.MimeTypeFilter
import com.bumptech.glide.Glide

/**
 * @author lgh
 * @date 2021/5/25
 *
 */
class ImageCacheInterceptor(val context: Context, config: CacheConfig) : ResourceInterceptor,
    Destroyable {

    private var mFilter = config.mimeTypeFilter


    override fun intercept(chain: ResourceInterceptor.Chain): WebResource? {
        val request = chain.request()
        if (mFilter?.isImageFilter(request?.mime) == true
            || mFilter?.isImageFilter(request?.mHeaders?.get("Content-Type")) == true
        ) {
            val img = Glide.with(context).asFile().load(request?.url).submit().get()
            if (img!=null && img.readBytes().isNotEmpty()){
                val webResource = WebResource()
                webResource.responseCode = 200
                webResource.originBytes = img.readBytes()
                return webResource
            }
        }
        return chain.process(request)
    }

    override fun destroy() {
        mFilter?.clear()
    }
}