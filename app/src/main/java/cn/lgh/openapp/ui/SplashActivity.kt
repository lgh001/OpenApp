package cn.lgh.openapp.ui

import android.content.Intent
import android.os.Handler
import android.os.Looper
import cn.lgh.openapp.ui.main.MainActivity
import cn.lgh.openapp.databinding.ActivitySplashBinding
import cn.lgh.openapp.ui.base.BaseActivity
import cn.lgh.openapp.ui.base.BaseViewModel
import cn.lgh.openapp.utils.PermissionUtil
import cn.lgh.openapp.utils.StatusBarUtil
import cn.lgh.openapp.widget.toast
import com.yanzhenjie.permission.runtime.Permission

/**
 * @author lgh
 * @date 2020/9/28
 *
 */
class SplashActivity : BaseActivity<BaseViewModel, ActivitySplashBinding>() {
    override fun initView() {
//        StatusBarUtil.immersive(this)
//        StatusBarUtil.darkMode(this)
        if (!this.isTaskRoot) {
            val mainIntent = intent
            val action = mainIntent.action
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }
        PermissionUtil.addPermission(this,
            Permission.Group.STORAGE,
            granted = {
                init()
            },
            denied = {
                toast("您拒绝了文件权限")

            })

    }

    private fun init() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(mContext, MainActivity::class.java))
            finish()
        }, 2000)
    }

    override fun initListener() {
    }

    override fun initData() {
    }

    override fun initVM() {
    }

}