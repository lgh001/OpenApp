package cn.lgh.openapp.widget.webview.cache.utils

/**
 * @author lgh
 * @date 2021/5/25
 *
 */
object MemorySizeCalculator {

    private const val MB = 1024 * 1024
    private const val MB_15 = 15 * MB
    private const val MB_10 = 10 * MB
    private const val MB_5 = 5 * MB

    fun getSize(): Int {
        val maxMemorySize = Runtime.getRuntime().maxMemory()
        val maxSizeByMB =
            (maxMemorySize / MB)
        if (maxSizeByMB >= 512) {
            return MB_15
        }
        if (maxSizeByMB >= 256) {
            return MB_10
        }
        return if (maxSizeByMB > 128) {
            MB_5
        } else 0
    }
}