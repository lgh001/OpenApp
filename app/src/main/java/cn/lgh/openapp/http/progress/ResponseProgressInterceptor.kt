package cn.lgh.openapp.http.progress

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author lgh
 * @date 2021/7/19
 * 下载进度条拦截器
 */
class ResponseProgressInterceptor(
    private val progressRequestListener: ProgressRequestListener
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val response= chain.proceed(chain.request())

        return response.newBuilder()
            .body(ProgressResponseBody(response.body,progressRequestListener))
            .build()
    }
}