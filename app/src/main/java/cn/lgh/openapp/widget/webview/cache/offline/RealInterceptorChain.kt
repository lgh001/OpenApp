package cn.lgh.openapp.widget.webview.cache.offline

/**
 * @author lgh
 * @date 2021/5/20
 * 一个普通的责任链
 */
class RealInterceptorChain(private val interceptors: List<ResourceInterceptor>) :
    ResourceInterceptor.Chain {

    private var mRequest: CacheRequest? = null
    private var mIndex = -1;

    override fun request(): CacheRequest? = mRequest

    override fun process(request: CacheRequest?): WebResource? {
        if (++mIndex >= interceptors.size) {
            return null
        }
        mRequest = request
        return interceptors[mIndex].intercept(this)
    }
}