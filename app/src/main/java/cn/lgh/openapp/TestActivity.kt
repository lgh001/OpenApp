package cn.lgh.openapp

import android.os.Bundle
import cn.lgh.openapp.databinding.ActivityTestBinding
import cn.lgh.openapp.ui.base.BaseActivity
import cn.lgh.openapp.ui.base.BaseViewModel
//import io.flutter.embedding.android.FlutterActivity
//import io.flutter.embedding.android.FlutterActivityLaunchConfigs

/**
 * @author lgh
 * @date 2020/12/14
 *
 */
class TestActivity : BaseActivity<BaseViewModel,ActivityTestBinding>() {
    override fun initView() {}

    override fun initListener() {
//        v.btnFlutter.setOnClickListener {
//            startActivity(
//                FlutterActivity.withCachedEngine("flutter_engine")
//                    .build(this)
//            )
//            overridePendingTransition(R.anim.left_in,R.anim.left_out)
//        }
    }

    override fun initData(bundle: Bundle?) {}

    override fun initVM() {}
}