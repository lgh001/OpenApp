package cn.lgh.openapp.ui.base.demo

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import cn.lgh.openapp.R
import cn.lgh.openapp.databinding.FragmentTestLazyBaseBinding
import cn.lgh.openapp.ui.base.BaseFragmentLazy
import cn.lgh.openapp.ui.base.BaseViewModel
import cn.lgh.openapp.ui.base.viewBindings

class TestLazyBaseFragment : BaseFragmentLazy() {

    override val vm by viewModels<BaseViewModel>()
    override val v by viewBindings(FragmentTestLazyBaseBinding::inflate)


    override fun initView() {
    }

    override fun initListener() {
    }

    override fun initData() {
    }

    override fun initVM() {
    }

    override fun lazyLoadData() {
    }

}