package cn.lgh.openapp.http.progress

/**
 * @author lgh
 * @date 2021/7/19
 *
 */
interface ProgressRequestListener {
    fun onRequestProgress(current: Long, total: Long, done: Boolean)
}