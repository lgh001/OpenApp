package cn.lgh.openapp.widget.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * @author lgh
 * @date 2020/9/30
 *
 */
class DefautFragmentStatePagerAdapter(
    private val fm: FragmentManager,
    datas: MutableList<FragmentInfo>?
) : BaseFragmentStatePagerAdapter(fm) {
    private var mDatas: MutableList<FragmentInfo> = datas ?: mutableListOf()


    override fun onCreateItem(position: Int): Fragment {
        val info = mDatas[position]
        val fragment = info.fragment.java.getConstructor().newInstance()
        fragment.arguments = info.data
        return fragment
    }

    override fun getCount(): Int = mDatas.size

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

}