package cn.lgh.openapp.ui.main.common

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.lgh.openapp.bean.WXArticleInfo
import cn.lgh.openapp.databinding.FragmentWxArticleBinding
import cn.lgh.openapp.ui.base.BaseFragment
import cn.lgh.openapp.ui.main.common.adapter.WXArticlesAdapter
import cn.lgh.openapp.ui.main.common.vm.CommonViewModel
import cn.lgh.openapp.ui.webview.WebViewActivity
import cn.lgh.openapp.utils.Utils
import cn.lgh.openapp.widget.ItemDecorationWidget

/**
 * @author lgh
 * @date 2020/12/17
 *
 */
class WXArticlesFragment : BaseFragment<CommonViewModel, FragmentWxArticleBinding>() {

    companion object {
        const val PARAM_KEY_ID = "PARAM_KEY_ID"
    }

    private val articles = mutableListOf<WXArticleInfo>()
    private var adapter: WXArticlesAdapter? = null
    private var mId = 0

    override fun initView() {
        v.recyclerView.layoutManager = LinearLayoutManager(context)
        v.recyclerView.addItemDecoration(
            ItemDecorationWidget(0, Utils.getRealPixel(20), includeHeadFooter = true)
        )

        adapter = WXArticlesAdapter(context!!, articles)
        v.recyclerView.adapter = adapter

        setupPageState(v.recyclerView)
    }

    override fun initListener() {
        adapter?.itemClick { _, wxArticleInfo ->
            WebViewActivity.start(context, wxArticleInfo.link, wxArticleInfo.title)
        }

        adapter?.onFavoriteClickListener = { info ->

        }
    }

    override fun initData() {}

    override fun initVM() {
        vm.articleList.observe(this, Observer {
            if (vm.isRefresh.value == true) {
                articles.clear()
            }
            val start = if (articles.size - 1 < 0) 0 else articles.size
            articles.addAll(it)
            adapter?.notifyItemRangeChanged(start, it.size)
//            v.recyclerView.adapter?.notifyDataSetChanged()
        })
    }

    override fun onLoadMore() {
        super.onLoadMore()
        vm.getArticlesById(mId)
    }

    override fun onRefresh() {
        super.onRefresh()
        vm.refreshArticles(mId)
    }

    override fun lazyLoadData() {
        mId = arguments?.getInt(PARAM_KEY_ID) ?: 0
//        vm.getArticlesById(mId)
        mRefreshLayout?.autoRefresh(50)
    }

    override fun hasRefresh(): Boolean = true
}