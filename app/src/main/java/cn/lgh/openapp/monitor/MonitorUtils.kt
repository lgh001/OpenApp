package cn.lgh.openapp.monitor

import android.content.Context
import android.provider.Settings
import java.io.BufferedReader
import java.io.FileReader

/**
 * @author lgh
 * @date 2021/7/23
 *
 */
object MonitorUtils {


    fun checkMultiApp(context: Context) {
        val isOpen =
            Settings.Secure.getInt(context.contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION)
        println("是否打开允许模拟位置：$isOpen")


        checkPkg(context)
    }

    private fun checkPkg(context: Context): Boolean {
        var count = 0;
        val pkgName = context.packageName;
        val pm = context.packageManager
        val pkgs = pm.getInstalledPackages(0)
        pkgs.forEach {
            if (pkgName.equals(it.packageName)) {
                count++
            }
        }
        println("同包名应用数：$count")
        return count > 1
    }

    private fun checkMaps(){
        val buff=BufferedReader(FileReader("/proc/self/maps"))
        var line:String
        while((buff.readLine().also { line = it })!=null){
            
        }
    }
}