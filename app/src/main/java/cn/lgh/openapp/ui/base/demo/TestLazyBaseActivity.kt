package cn.lgh.openapp.ui.base.demo

import android.os.Bundle
import androidx.activity.viewModels
import cn.lgh.openapp.ui.base.BaseActivityLazy
import cn.lgh.openapp.ui.base.BaseViewModel
import cn.lgh.openapp.ui.base.viewBindings
import cn.lgh.openapp.databinding.ActivityTestLazyBaseBinding

class TestLazyBaseActivity : BaseActivityLazy() {
    override val vm by viewModels<BaseViewModel>()
    override val v by viewBindings(ActivityTestLazyBaseBinding::inflate)

    override fun initView() {
    }

    override fun initListener() {
    }

    override fun initData(bundle: Bundle?) {
    }

    override fun initVM() {
    }

}