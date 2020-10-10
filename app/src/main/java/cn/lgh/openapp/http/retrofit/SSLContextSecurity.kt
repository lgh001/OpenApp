package cn.lgh.openapp.http.retrofit

import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

/**
 * @author lgh
 * @date 2020/9/27
 *
 */
object SSLContextSecurity {

    fun createIgnoreVerifySSL(sslVersion:String):SSLSocketFactory{
        var sc=SSLContext.getInstance(sslVersion)
        val trustAllCerts:Array<TrustManager> = arrayOf(object :X509TrustManager{
            override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            }

            override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate?> {
                return arrayOfNulls(0)
            }

        })
        sc!!.init(null,trustAllCerts,SecureRandom())
//        val allHostsValid=HostnameVerifier(_ , _->true)
        val allHostsValid = HostnameVerifier { _, _ -> true }
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
        return sc.socketFactory
    }
}

class MyX509TrustManager : X509TrustManager{
    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {
    }

    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate?> {
        return arrayOfNulls(0)
    }

}