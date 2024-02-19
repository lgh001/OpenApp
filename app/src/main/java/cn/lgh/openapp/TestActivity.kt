package cn.lgh.openapp

import android.Manifest
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import cn.lgh.openapp.databinding.ActivityTestBinding
import cn.lgh.openapp.test.PhoneCallReceiver
import cn.lgh.openapp.ui.base.BaseActivity
import cn.lgh.openapp.ui.base.BaseActivityLazy
import cn.lgh.openapp.ui.base.BaseViewModel
import cn.lgh.openapp.ui.base.viewBindings
import cn.lgh.openapp.utils.PermissionUtil

/**
 * @author lgh
 * @date 2020/12/14
 *
 */
class TestActivity : BaseActivityLazy() {
    override val vm by viewModels<BaseViewModel>();
    override val v by viewBindings(ActivityTestBinding::inflate)

    private var phoneCall: PhoneCallReceiver?=null
    override fun initView() {
        PermissionUtil.addPermission(this, arrayListOf(Manifest.permission.READ_PHONE_STATE), granted = {
            phoneCall = PhoneCallReceiver(this)
            phoneCall?.startListening()
        })

    }

    override fun initListener() {
//        v.btnFlutter.setOnClickListener {
//            startActivity(
//                FlutterActivity.withCachedEngine("flutter_engine")
//                    .build(this)
//            )
//            overridePendingTransition(R.anim.left_in,R.anim.left_out)
//        }
        v.btnFlutter.setOnClickListener {
            Handler(mainLooper).postDelayed({
                phoneCall?.start()
            },5000)

        }
    }

    override fun initData(bundle: Bundle?) {}

    override fun initVM() {}

    fun getAllThreads():MutableList<Thread>{
        var group = Thread.currentThread().threadGroup;
        var system:ThreadGroup?
        do{
            system = group
            group = group?.parent
        }while (group!=null)
        val count = system?.activeCount()?:0
        val threads = arrayOfNulls<Thread>(count)
        system?.enumerate(threads)
        val list = mutableListOf<Thread>()
        threads.forEach {
            it?.let { it1-> list.add(it1) }
        }
        return list
    }

    override fun onDestroy() {
        super.onDestroy()
        phoneCall?.stopListening()
    }
}