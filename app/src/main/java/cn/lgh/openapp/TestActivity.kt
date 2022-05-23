package cn.lgh.openapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.viewbinding.ViewBinding
import cn.lgh.openapp.databinding.ActivityTestBinding
import cn.lgh.openapp.ui.base.BaseActivity
import cn.lgh.openapp.ui.base.BaseActivityLazy
import cn.lgh.openapp.ui.base.BaseViewModel
import cn.lgh.openapp.ui.base.viewBindings

/**
 * @author lgh
 * @date 2020/12/14
 *
 */
class TestActivity : BaseActivityLazy() {
    override val vm by viewModels<BaseViewModel>();
    override val v by viewBindings(ActivityTestBinding::inflate)

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
}