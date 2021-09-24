package cn.lgh.openapp.http

import cn.lgh.openapp.http.ssl.SSLHelper
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

/**
 * @author lgh
 * @date 2021/7/19
 *
 */
class OkHttpProvider private constructor() {

    private object Holder {
        val instance = OkHttpProvider()
    }

    companion object {
        fun getInstance(): OkHttpProvider = Holder.instance
    }

    private lateinit var mClient: OkHttpClient
    private val timeOut = 30 * 1000L

    init {
        createClient()
    }

    private fun createClient() {
        val builder = OkHttpClient.Builder()
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)
            .connectTimeout(timeOut, TimeUnit.SECONDS)

        builder.sslSocketFactory(SSLHelper.createSSLSocketFactory(), SSLHelper.TrustAllManager())
        builder.hostnameVerifier(SSLHelper.TrustAllHostnameVerifier())

        //证书信任链，只有指定的证书能通过
//			CertificatePinner certificatePinner = new CertificatePinner.Builder()
//					//正常请求下的证书验证链路
//					.add("news-at.zhihu.com", "sha256/f5fNYvDJUKFsO51UowKkyKAlWXZXpaGK6Bah4yX9zmI=")//CN=*.zhihu.com,OU=IT,O=智者四海（北京）技术有限公司,L=北京市,C=CN
//					.build();
//			builder.certificatePinner(certificatePinner);

        mClient = builder.build()
    }


    fun doGet(url: String?): String? {
        if (url.isNullOrEmpty()) return null
        val client = get()
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        val response = client.newCall(request).execute()
        return response.body.toString()
    }

    fun doPost(url: String?, param: MultipartBody.Builder): String? {
        if (url.isNullOrEmpty()) return null
        val client = get()
        param.setType(MultipartBody.FORM)
        val request = Request.Builder()
            .url(url)
            .method("POST", param.build())
            .build()
        val response = client.newCall(request).execute()
        return response.body.toString()
    }

    fun get(): OkHttpClient = mClient
}