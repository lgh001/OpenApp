package cn.lgh.openapp.widget.viewpager

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * @author lgh
 * @date 2020/9/30
 *
 */
abstract class BaseFragmentStatePagerAdapter(fm:FragmentManager) : FragmentStatePagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    var mList = mutableListOf<Fragment>()

    override fun getItem(position: Int): Fragment {
        val item=onCreateItem(position)
        mList.add(item)
        return item
    }

    abstract fun onCreateItem(position: Int):Fragment

    fun getFragment(position: Int):Fragment?{
        if (position>=mList.size){
            return null
        }
        return mList[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)
        mList.removeAt(position)
    }
}