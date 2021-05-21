package cn.lgh.openapp.widget.webview.cache.okhttp

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * @author lgh
 * @date 2021/5/21
 *
 */
class OkHttpClientProvider private constructor() {

    companion object {

        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            OkHttpClientProvider()
        }

        fun get(): OkHttpClient {
            return instance.mHttpClient
        }
    }

    private val mHttpClient by lazy {
        OkHttpClient.Builder()
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .connectTimeout(20, TimeUnit.SECONDS)
            .followSslRedirects(false)
            .followRedirects(false)
            .build()
    }


}