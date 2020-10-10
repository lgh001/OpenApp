package cn.lgh.openapp.http

import cn.lgh.openapp.http.retrofit.RetrofitClient

/**
 * @author lgh
 * @date 2020/9/27
 * 多封一层
 */
class HttpUtil {

    private val mService by lazy {
        RetrofitClient.getInstance().create()
    }

    companion object {
        @Volatile
        private var httpUtil: HttpUtil? = null

        fun getInstance() = httpUtil ?: synchronized(this) {
            httpUtil ?: HttpUtil().also { httpUtil = it }
        }
    }

    fun getService(): ApiService = mService
}