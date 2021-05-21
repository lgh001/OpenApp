package cn.lgh.openapp.widget.webview.cache.utils

import android.util.Log

/**
 * @author lgh
 * @date 2021/5/20
 *
 */
object CacheLog {

    fun e(msg:String?){
        Log.e("CacheLog", "$msg")
    }

    fun d(msg:String?){
        Log.d("CacheLog", "$msg")
    }
}