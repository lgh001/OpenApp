package cn.lgh.openapp.widget.webview.cache.offline

import java.io.IOException


/**
 * @author lgh
 * @date 2021/5/20
 * 拦截器
 */
interface ResourceInterceptor {

    fun intercept(chain:Chain): WebResource?

    interface Chain{
        fun request():CacheRequest?

        @Throws(IOException::class)
        fun process(request:CacheRequest?):WebResource?
    }
}