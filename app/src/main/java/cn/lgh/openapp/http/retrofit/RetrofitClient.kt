package cn.lgh.openapp.http.retrofit

import cn.lgh.openapp.http.ApiService
import cn.lgh.openapp.http.URLConstants
import cn.lgh.openapp.http.intercept.InterceptorUtil
import cn.lgh.openapp.http.ssl.SSLHelper
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author lgh
 * @date 2020/9/27
 *
 */
class RetrofitClient {

    companion object {
        fun getInstance() = SingleInstance.INSTANCE

        private lateinit var retrofit: Retrofit
    }

    private object SingleInstance {
        val INSTANCE = RetrofitClient()
    }

    init {
        retrofit = Retrofit.Builder()
            .client(getHttpClient())
            .baseUrl(URLConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    private var cookieJar: PersistentCookieJar = PersistentCookieJar(
//        SetCookieCache(),
//        SharedPrefsCookiePersistor(App.instance)
//    )

    fun getHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(InterceptorUtil.getLogInterceptor())
            .addInterceptor(InterceptorUtil.getInterceptor())
            .sslSocketFactory(SSLHelper.createSSLSocketFactory(), SSLHelper.TrustAllManager())
            .hostnameVerifier(SSLHelper.TrustAllHostnameVerifier())
            .build()
    }

    fun create(): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}