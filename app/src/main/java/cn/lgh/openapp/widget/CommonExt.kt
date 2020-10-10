package cn.lgh.openapp.widget

import android.content.Intent

/**
 * @author lgh
 * @date 2020/9/29
 * 一些公共的扩展函数
 */


/**
 * 不报错运行
 */
inline fun <T,R> T.runSafely(block:(T)->R)=try {
    block(this)
}catch (e:Exception){
    e.printStackTrace()
    null
}

/**
 * 可空对象转非空对象
 */
inline fun <O> O?.runIfNonNull(block: (O) -> Unit){
    this?.let { block(it) }
}

/**
 * String 转Intent对象
 */
fun String.toIntent(flags:Int=0):Intent=Intent(this).setFlags(flags)