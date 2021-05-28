package cn.lgh.openapp.widget.webview.cache.offline

import cn.lgh.openapp.widget.webview.cache.loader.OkHttpResourceLoader
import cn.lgh.openapp.widget.webview.cache.loader.SourceRequest

/**
 * @author lgh
 * @date 2021/5/21
 *
 */
class DefaultRemoteResourceInterceptor : ResourceInterceptor {

    private val mOkhttpLoader by lazy {
        OkHttpResourceLoader()
    }

    override fun intercept(chain: ResourceInterceptor.Chain): WebResource? {
        val request = chain.request()
        val sourceRequest = SourceRequest(request, true)
        val resource = mOkhttpLoader.getResource(sourceRequest)
        if (resource != null) {
            return resource
        }
        return chain.process(request)
    }
}