package cn.lgh.openapp.ui.main

import androidx.lifecycle.MutableLiveData
import cn.lgh.openapp.ui.base.BaseViewModel
import cn.lgh.openapp.widget.viewpager.FragmentInfo

/**
 * @author lgh
 * @date 2020/9/28
 *
 */
class MainViewModel : BaseViewModel() {

    val fragments: MutableLiveData<MutableList<FragmentInfo>> =
        MutableLiveData<MutableList<FragmentInfo>>().apply {
            mutableListOf<FragmentInfo>().apply {
//                add(FragmentInfo(fragment = HomeFragment::class))
//                add(FragmentInfo(fragment = CateFragment::class))
//                add(FragmentInfo(fragment = CommonFragment::class))
            }
        }


}