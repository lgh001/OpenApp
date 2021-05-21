package cn.lgh.openapp.widget.webview.cache.offline

import android.webkit.WebResourceResponse
import cn.lgh.openapp.widget.webview.cache.config.CacheConfig

/**
 * @author lgh
 * @date 2021/5/21
 *
 */
class OfflineServerImpl(val config: CacheConfig) : OfflineServer, Destroyable {

    private var mBaseInterceptorChain = mutableListOf<ResourceInterceptor>()
    private var mDefaultModeChain: List<ResourceInterceptor>? = null
    private var mForceModeChain: List<ResourceInterceptor>? = null

    private val mWebResourceResponseGenerator by lazy { DefaultWebResponseGenerator() }

    private fun buildDefaultInterceptorChain(): List<ResourceInterceptor>? {
        if (mDefaultModeChain == null) {
            val list = mutableListOf<ResourceInterceptor>()
            list.addAll(mBaseInterceptorChain)
            list.add(MemoryCacheInterceptor(config))
            list.add(DiskCacheInterceptor(config))
            list.add(ForceRemoteResourceInterceptor(config))
            mDefaultModeChain = list
        }
        return mDefaultModeChain
    }

    private fun buildForceInterceptorChain(): List<ResourceInterceptor>? {
        if (mForceModeChain == null) {
            val list = mutableListOf<ResourceInterceptor>()
            list.addAll(mBaseInterceptorChain)
            list.add(DefaultRemoteResourceInterceptor())
            mForceModeChain = list
        }
        return mForceModeChain
    }

    override fun get(request: CacheRequest): WebResourceResponse? {
        val forceMode = request.forceMode
        val interceptors =
            if (forceMode) buildForceInterceptorChain() else buildDefaultInterceptorChain()
        val chain = RealInterceptorChain(interceptors ?: mutableListOf())
        val webResource = chain.process(request)
        return mWebResourceResponseGenerator.generate(webResource, request.mime)
    }

    @Synchronized
    override fun addResourceInterceptor(interceptor: ResourceInterceptor) {
        mBaseInterceptorChain.add(interceptor)
    }

    override fun destroy() {
        destroyAll(mDefaultModeChain)
        destroyAll(mForceModeChain)
    }

    private fun destroyAll(interceptors: List<ResourceInterceptor>?) {
        if (interceptors == null || interceptors.isEmpty()) {
            return
        }
        for (interceptor in interceptors) {
            if (interceptor is Destroyable) {
                (interceptor as Destroyable).destroy()
            }
        }
    }
}