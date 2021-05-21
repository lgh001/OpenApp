package cn.lgh.openapp.widget.webview

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * @author lgh
 * @date 2021/5/19
 *
 */
class WebStartService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        val name=ProcessUtils.getAppProcess(baseContext)
        println("服务:$name")
        super.onCreate()
    }

}