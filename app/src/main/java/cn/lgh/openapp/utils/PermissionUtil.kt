package cn.lgh.openapp.utils

import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX

/**
 * @author lgh
 * @date 2020/9/28
 *
 */
object PermissionUtil {

    fun addPermission(
        activity: FragmentActivity,
        permissions: List<String>,
        granted: ((MutableList<String>) -> Unit)? = null,
        denied: ((MutableList<String>) -> Unit)? = null
    ) {
        PermissionX.init(activity).permissions(permissions)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    granted?.invoke(grantedList)
                } else {
                    denied?.invoke(deniedList)
                }
            }
//        AndPermission
//            .with(context)
//            .runtime()
//            .permission(permissions)
//            .onGranted {
//                granted?.invoke(it)
//            }
//            .onDenied {
//                denied?.invoke(it)
//            }
//            .start()
    }
}