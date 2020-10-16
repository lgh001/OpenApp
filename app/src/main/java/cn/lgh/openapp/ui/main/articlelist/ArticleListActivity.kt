package cn.lgh.openapp.ui.main.articlelist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.lgh.openapp.bean.ArticleInfo
import cn.lgh.openapp.databinding.ActivityArticleListBinding
import cn.lgh.openapp.ui.base.BaseActivity
import cn.lgh.openapp.ui.main.articlelist.vm.ArticleListViewModel
import cn.lgh.openapp.ui.main.home.adapter.ArticleAdapter
import cn.lgh.openapp.utils.Utils
import cn.lgh.openapp.widget.ItemDecorationWidget
import cn.lgh.openapp.widget.toast
import kotlinx.android.synthetic.main.view_title_bar.*

/**
 * @author lgh
 * @date 2020/10/12
 *
 */
class ArticleListActivity : BaseActivity<ArticleListViewModel, ActivityArticleListBinding>() {

    companion object {
        const val CATE_ID = "CATE_ID"
        const val CATE_NAME = "CATE_NAME"

        fun start(context: Context, id: Int, name: String) {
            val intent = Intent(context, ArticleListActivity::class.java)
            intent.putExtra(CATE_ID, id)
            intent.putExtra(CATE_NAME, name)
            context.startActivity(intent)
        }
    }

    private val articleList = mutableListOf<ArticleInfo>()
    private var mAdapter: ArticleAdapter? = null


    override fun initView() {
        v.recyclerView.layoutManager = LinearLayoutManager(this)
        v.recyclerView.addItemDecoration(ItemDecorationWidget(0, Utils.getRealPixel(20)))

        mAdapter = ArticleAdapter(articleList)
        v.recyclerView.adapter = mAdapter
        mRefreshLayout?.setEnableRefresh(false)

//        mPageState.setupContentView(v.recyclerView)
    }

    override fun initListener() {
        mAdapter?.setOnItemChildClickListener { adapter, view, position ->
            toast(articleList[position].title)
        }

        iv_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun initData(bundle: Bundle?) {
        intent?.apply {
            vm.id = getIntExtra(CATE_ID, 0)
            vm.getArticleList()

            tv_title.text = getStringExtra(CATE_NAME)
        }
    }

    override fun initVM() {
        vm.articleList.observe(this, Observer {
            articleList.addAll(it)
            mAdapter?.notifyDataSetChanged()
        })
    }

    override fun onLoadMore() {
        super.onLoadMore()
        vm.getArticleList()
    }

    override fun hasRefresh(): Boolean {
        return true
    }
}