package cn.lgh.openapp.widget.webview

import android.app.ActivityManager
import android.content.Context

/**
 * @author lgh
 * @date 2021/5/19
 *
 */
object ProcessUtils {

    /**
     * 获取当前进程名
     * @param context Context
     * @return String?
     */
    fun getAppProcess(context: Context): String? {
        val currentId = android.os.Process.myPid()
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = manager.runningAppProcesses
        for (info in list) {
            if (currentId == info.pid) {
                return info.processName
            }
        }

        return null
    }

}