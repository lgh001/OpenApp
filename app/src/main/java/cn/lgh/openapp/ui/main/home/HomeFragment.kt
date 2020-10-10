package cn.lgh.openapp.ui.main.home

import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.lgh.openapp.R
import cn.lgh.openapp.bean.ArticleInfo
import cn.lgh.openapp.bean.BannerInfo
import cn.lgh.openapp.databinding.FragmentHomeBinding
import cn.lgh.openapp.ui.base.BaseFragment
import cn.lgh.openapp.ui.main.home.adapter.HomeAdapter
import cn.lgh.openapp.ui.main.home.adapter.HomeBannerAdapter
import cn.lgh.openapp.ui.main.home.vm.HomeViewModel
import cn.lgh.openapp.utils.Utils
import cn.lgh.openapp.widget.ItemDecorationWidget
import cn.lgh.openapp.widget.toast
import com.youth.banner.Banner

/**
 * @author lgh
 * @date 2020/9/30
 *
 */
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    var adapter: HomeAdapter? = null
    var banner: Banner<BannerInfo, HomeBannerAdapter>? = null

    private val listData = mutableListOf<ArticleInfo>()


    override fun initView() {
        v.recyclerView.layoutManager = LinearLayoutManager(mContext)
        v.recyclerView.addItemDecoration(
            ItemDecorationWidget(0, Utils.getRealPixel(20),includeHeadFooter = false))

        adapter = HomeAdapter(listData)
        v.recyclerView.adapter = adapter

        val view = layoutInflater.inflate(R.layout.view_home_banner, null) as ViewGroup
        banner = view.findViewById(R.id.banner)
        view.removeView(banner)
        banner?.apply {
            addBannerLifecycleObserver(this@HomeFragment)
            isAutoLoop(true)
            setLoopTime(5000)
            setScrollTime(800)
        }
        adapter?.addHeaderView(banner!!)

        mRefreshLayout?.setEnableLoadMore(true)
    }

    override fun initListener() {
        adapter?.setOnItemClickListener { adapter, view, position ->
            toast(listData[position].link)
        }
    }

    override fun initData() {
    }

    override fun initVM() {
        vm.banner.observe(this, Observer {
            val bannerAdapter = HomeBannerAdapter(it)
            banner?.adapter = bannerAdapter
        })
        vm.articleListData.observe(this, Observer {
            listData.addAll(it)
            v.recyclerView.adapter?.notifyDataSetChanged()
        })
    }

    override fun lazyLoadData() {
        vm.getBannerData()
        vm.getArticleList()
    }

    override fun onRefresh() {
        super.onRefresh()
        vm.getBannerData()
        vm.refreshList()
    }

    override fun onLoadMore() {
        super.onLoadMore()
        vm.getArticleList()
    }

    override fun hasRefresh(): Boolean = true

}