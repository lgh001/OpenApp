package cn.lgh.openapp.utils

import com.tencent.mmkv.MMKV

/**
 * @author lgh
 * @date 2021/6/30
 *
 */
object SPUtils {

    val mmkv by lazy {
        MMKV.defaultMMKV()

        //多进程
//        MMKV.mmkvWithID("test",MMKV.MULTI_PROCESS_MODE)
    }

    var token: String?
        get() = mmkv.decodeString("token", "")
        set(value) {
            mmkv.encode("token", value)
        }

    fun clear(){
        mmkv.clearAll()
    }
}