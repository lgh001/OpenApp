package cn.lgh.openapp.http.progress

import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author lgh
 * @date 2021/7/19
 * 上传进度条拦截器
 */
class RequestProgressInterceptor(
    private val progressRequestListener: ProgressRequestListener
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val request = original.newBuilder()
            .method(original.method, ProgressRequestBody(original.body, progressRequestListener))
            .build()

        return chain.proceed(request)
    }
}