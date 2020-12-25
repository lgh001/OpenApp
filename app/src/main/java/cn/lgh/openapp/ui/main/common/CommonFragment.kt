package cn.lgh.openapp.ui.main.common

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.lgh.openapp.databinding.FragmentCommonBinding
import cn.lgh.openapp.ui.base.BaseFragment
import cn.lgh.openapp.ui.main.common.vm.CommonViewModel
import cn.lgh.openapp.utils.Utils
import cn.lgh.openapp.widget.viewpager.BaseFragmentStatePagerAdapter
import cn.lgh.openapp.widget.viewpager.DefautFragmentStatePagerAdapter
import cn.lgh.openapp.widget.viewpager.FragmentInfo

/**
 * @author lgh
 * @date 2020/9/30
 *
 */
class CommonFragment : BaseFragment<CommonViewModel, FragmentCommonBinding>() {

    private val fragments = mutableListOf<FragmentInfo>()
//        .apply {
//        add(FragmentInfo(fragment = WXArticlesFragment::class, data = Bundle().apply {
//            putInt(WXArticlesFragment.PARAM_KEY_ID, 410)
//        }))
//        add(FragmentInfo(WXArticlesFragment::class, Bundle().apply {
//            putInt(WXArticlesFragment.PARAM_KEY_ID, 410)
//        }))
//    }
    private var adapter: BaseFragmentStatePagerAdapter? = null

    override fun initView() {
        v.tabLayout.setHasRoundCorner(true)
        v.tabLayout.setHasRoundCorner(true)
        v.tabLayout.setSelectedIndicatorWidth(Utils.getRealPixel(50))

//        adapter = DefautFragmentStatePagerAdapter(fragmentManager!!, fragments)
//        v.viewpager.adapter = adapter

//        v.tabLayout.setupWithViewPager(v.viewpager)
//        setupPageState(v.viewpager)
    }

    override fun initListener() {}

    override fun initData() {}

    override fun initVM() {
        vm.authorList.observe(this, Observer {
            for (author in it) {
                val fragment = FragmentInfo(WXArticlesFragment::class, Bundle().apply {
                    putInt(WXArticlesFragment.PARAM_KEY_ID, author.id)
                })
                fragments.add(fragment)
                v.tabLayout.addTab(v.tabLayout.newTab())
            }
            adapter = DefautFragmentStatePagerAdapter(fragmentManager!!, fragments)
            v.viewpager.adapter = adapter
//            adapter?.notifyDataSetChanged()
//            v.tabLayout.setupWithViewPager(v.viewpager)

            for (i in 0 until it.size) {
                v.tabLayout.getTabAt(i)?.text = it[i].name
            }
        })
    }

    override fun lazyLoadData() {
        vm.getAuthorList()
    }

    override fun hasRefresh(): Boolean = false
}