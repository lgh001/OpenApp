package cn.lgh.openapp.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
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
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    lateinit var mFragmentAdapter: BaseFragmentStatePagerAdapter

    private val fragments = mutableListOf<FragmentInfo>().apply {
        add(FragmentInfo(fragment = HomeFragment::class))
        add(FragmentInfo(fragment = KnowledgeTreeFragment::class))
        add(FragmentInfo(fragment = CommonFragment::class))
    }

    private val fragments1 = mutableListOf<Fragment>()

    private var currentItem = 0

    override fun initView() {
//        ImmersionBar.with(this)
//            .transparentBar()
//            .statusBarColor(R.color.app_main_color)
//            .titleBarMarginTop(container)
//            .autoDarkModeEnable(true)
//            .init()
        mFragmentAdapter =
            DefautFragmentStatePagerAdapter(supportFragmentManager, fragments)
        v.viewpager.adapter = mFragmentAdapter
        v.viewpager.setNoScroll(true)
        //最大存活数量
        v.viewpager.offscreenPageLimit = vm.fragments.value?.size ?: 3
//        fragments1.add(HomeFragment())
//        fragments1.add(KnowledgeTreeFragment())
//        fragments1.add(CommonFragment())
//        loadRootMultiFragment(
//            R.id.container1,
//            0,
//            fragments1[0],
//            fragments1[1],
//            fragments1[2]
//        )
    }

    override fun initListener() {
        v.mainNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    showTab(0)
                    v.tvTitle.text = getString(R.string.main_tab_home_text)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_cate -> {
                    showTab(1)
                    v.tvTitle.text = getString(R.string.main_tab_cate_text)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_common -> {
                    showTab(2)
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

//        v.avatar.setOnClickListener {
//            startActivity(
//                FlutterActivity.withCachedEngine("flutter_engine")
//                    .build(this)
//            )
//        }
    }

    private fun showTab(index: Int) {
//        showAndHideFragment(fragments1[index], fragments1[currentItem])
//        currentItem = index
        v.viewpager.currentItem = index
    }

    override fun initData(bundle: Bundle?) {
    }

    override fun initVM() {
    }

    private var lastClickTime = 0L
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