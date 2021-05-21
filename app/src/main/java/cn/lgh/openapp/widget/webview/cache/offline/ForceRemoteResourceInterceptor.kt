package cn.lgh.openapp.widget.webview.cache.offline

import cn.lgh.openapp.widget.webview.cache.config.CacheConfig
import cn.lgh.openapp.widget.webview.cache.loader.OkHttpResourceLoader
import cn.lgh.openapp.widget.webview.cache.loader.SourceRequest

/**
 * @author lgh
 * @date 2021/5/21
 *
 */
class ForceRemoteResourceInterceptor(config: CacheConfig) : ResourceInterceptor, Destroyable {

    private val mResourceLoader by lazy { OkHttpResourceLoader() }
    private val mMimeTypeFilter = config.mimeTypeFilter


    override fun intercept(chain: ResourceInterceptor.Chain): WebResource? {
        val request = chain.request()
        val isFilter = if (request?.mime == null) {
            isFilterHtml()
        } else {
            mMimeTypeFilter?.isFilter(request.mime) ?: false
        }
        val sourceRequest = SourceRequest(request, isFilter)
        val resource = mResourceLoader.getResource(sourceRequest)
        if (resource != null) return resource
        return chain.process(request)
    }

    private fun isFilterHtml(): Boolean {
        //一般来说 html不缓存
        return mMimeTypeFilter?.isFilter("text/html") ?: false
    }

    override fun destroy() {
        mMimeTypeFilter?.clear()
    }
}