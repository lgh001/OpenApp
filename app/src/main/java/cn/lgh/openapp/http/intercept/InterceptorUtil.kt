package cn.lgh.openapp.http.intercept

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

/**
 * @author lgh
 * @date 2020/10/10
 *
 */
object InterceptorUtil{


    fun getLogInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger{
            override fun log(message: String) {
                Log.i("http", "log: "+message)
            }

        }).setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    fun getInterceptor():Interceptor{
        return object : Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val request=chain.request()
                val response=chain.proceed(request)
                val buffer=response.body?.source()?.buffer?.copy()
                val res=buffer.toString()
                Log.i("http", "intercept: "+res)
                return response
            }
        }
    }
}
