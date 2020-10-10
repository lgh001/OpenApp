package cn.lgh.openapp.utils

import android.content.Context
import com.yanzhenjie.permission.AndPermission

/**
 * @author lgh
 * @date 2020/9/28
 *
 */
object PermissionUtil {

    fun addPermission(
        context: Context,
        permissions: Array<String>,
        granted: ((MutableList<String>) -> Unit)? = null,
        denied: ((MutableList<String>) -> Unit)? = null
    ) {
        AndPermission
            .with(context)
            .runtime()
            .permission(permissions)
            .onGranted {
                granted?.invoke(it)
            }
            .onDenied {
                denied?.invoke(it)
            }
            .start()
    }
}