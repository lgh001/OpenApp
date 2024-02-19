package cn.lgh.openapp.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import cn.lgh.openapp.R
import cn.lgh.openapp.ui.main.MainActivity
import cn.lgh.openapp.databinding.ActivitySplashBinding
import cn.lgh.openapp.ui.base.BaseActivity
import cn.lgh.openapp.ui.base.BaseViewModel
import com.gyf.immersionbar.ImmersionBar

/**
 * @author lgh
 * @date 2020/9/28
 *
 */
class SplashActivity : BaseActivity<BaseViewModel, ActivitySplashBinding>() {
    override fun initView() {
        ImmersionBar.with(this)
            .transparentBar()
            .statusBarDarkFont(true)
            .init()
        if (!this.isTaskRoot) {
            val mainIntent = intent
            val action = mainIntent.action
            if (mainIntent.hasCategory(Intent.CATEGORY_LAUNCHER) && action == Intent.ACTION_MAIN) {
                finish()
                return
            }
        }
//        PermissionUtil.addPermission(this,
//            arrayListOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
//            granted = {
//                init()
//            },
//            denied = {
//                toast("您拒绝了文件权限")
//
//            })
        init()
        v.flyView.setText("Believe In Technology")
        v.flyView.setTextColor(resources.getColor(R.color.app_main_color))
    }

    private fun init() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(mContext, MainActivity::class.java))
            finish()
        }, 2000)
    }

    override fun initListener() {
    }

    override fun initVM() {
    }

    override fun initData(bundle: Bundle?) {
    }

}