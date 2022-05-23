package cn.lgh.openapp.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings

import android.os.Build





object NotificationUtils {


    //跳转到通知管理
    fun gotoNotificationSetting(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val intent = Intent()
            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            intent.putExtra("app_package", activity.packageName)
            intent.putExtra("app_uid",activity.applicationInfo.uid)
            activity.startActivity(intent)
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.data = Uri.parse("package:" + activity.packageName)
            activity.startActivity(intent)
        }
    }
}