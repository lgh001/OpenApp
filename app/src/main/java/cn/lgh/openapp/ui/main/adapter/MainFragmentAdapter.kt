package cn.lgh.openapp.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import cn.lgh.openapp.widget.viewpager.BaseFragmentStatePagerAdapter
import cn.lgh.openapp.widget.viewpager.FragmentInfo

/**
 * @author lgh
 * @date 2020/9/30
 *
 */
class MainFragmentAdapter(
    private val fm: FragmentManager,
    private val mDatas: MutableList<FragmentInfo>
) : BaseFragmentStatePagerAdapter(fm) {


    override fun onCreateItem(position: Int): Fragment {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }
}