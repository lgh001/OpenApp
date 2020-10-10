package cn.lgh.openapp.ui.main

import cn.lgh.openapp.R
import cn.lgh.openapp.databinding.ActivityMainBinding
import cn.lgh.openapp.ui.base.BaseActivity
import cn.lgh.openapp.ui.main.knowledgetree.KnowledgeTreeFragment
import cn.lgh.openapp.ui.main.common.CommonFragment
import cn.lgh.openapp.ui.main.home.HomeFragment
import cn.lgh.openapp.widget.toast
import cn.lgh.openapp.widget.viewpager.BaseFragmentStatePagerAdapter
import cn.lgh.openapp.widget.viewpager.DefautFragmentStatePagerAdapter
import cn.lgh.openapp.widget.viewpager.FragmentInfo
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    lateinit var mFragmentAdapter: BaseFragmentStatePagerAdapter

    private val fragments = mutableListOf<FragmentInfo>().apply {
        add(FragmentInfo(fragment = HomeFragment::class))
        add(FragmentInfo(fragment = KnowledgeTreeFragment::class))
        add(FragmentInfo(fragment = CommonFragment::class))
    }

    override fun initView() {
        ImmersionBar.with(this)
            .transparentBar()
            .statusBarColor(R.color.app_main_color)
            .titleBarMarginTop(container)
            .autoDarkModeEnable(true)
            .init()
        mFragmentAdapter =
            DefautFragmentStatePagerAdapter(supportFragmentManager, fragments)
        v.viewpager.adapter = mFragmentAdapter
        v.viewpager.setNoScroll(true)
        //最大存活数量
        v.viewpager.offscreenPageLimit = vm.fragments.value?.size ?: 3
    }

    override fun initListener() {
        v.mainNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    v.viewpager.currentItem = 0
                    v.tvTitle.text = getString(R.string.main_tab_home_text)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_cate -> {
                    v.viewpager.currentItem = 1
                    v.tvTitle.text = getString(R.string.main_tab_cate_text)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_common -> {
                    v.viewpager.currentItem = 2
                    v.tvTitle.text = getString(R.string.main_tab_common_text)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        //搜索
        v.ivSearch.setOnClickListener {

        }
//        findViewById<FlowingDrawer>(R.id.drawer_layout)
        //打开抽屉
        v.ivMenu.setOnClickListener {
            if (!drawer_layout.isMenuVisible) {
                drawer_layout.openMenu(true)
            } else {
                drawer_layout.closeMenu(true)
            }
        }
    }

    override fun initData() {
    }

    override fun initVM() {
    }

    var lastClickTime = 0L
    override fun onBackPressed() {
        if (drawer_layout.isMenuVisible) {
            drawer_layout.closeMenu(true)
            return
        }

        if (System.currentTimeMillis() - lastClickTime > 2000) {
            lastClickTime = System.currentTimeMillis()
            toast("再点一次退出app")
            return
        }
        super.onBackPressed()
    }

}