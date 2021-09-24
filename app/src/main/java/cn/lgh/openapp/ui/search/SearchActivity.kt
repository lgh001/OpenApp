package cn.lgh.openapp.ui.search

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import cn.lgh.openapp.bean.ArticleInfo
import cn.lgh.openapp.databinding.ActivitySearchBinding
import cn.lgh.openapp.ui.base.BaseActivity
import cn.lgh.openapp.ui.webview.WebViewActivity
import cn.lgh.openapp.ui.search.adapter.SearchAdapter
import cn.lgh.openapp.ui.search.vm.SearchViewModel
import cn.lgh.openapp.utils.Utils
import cn.lgh.openapp.widget.ItemDecorationWidget

/**
 * @author lgh
 * @date 2021/6/28
 *
 */
class SearchActivity : BaseActivity<SearchViewModel, ActivitySearchBinding>() {


    companion object {

        fun start(
            context: Activity,
            translationView: View? = null,
            translationName: String = "search"
        ) {
            val intent = Intent(context, SearchActivity::class.java)
            if (translationView == null) {
                context.startActivity(intent)
            } else {
                context.startActivity(
                    intent,
                    ActivityOptions.makeSceneTransitionAnimation(
                        context,
                        translationView,
                        translationName
                    ).toBundle()
                )
            }
        }
    }

    private var adapter: SearchAdapter? = null
    private val datas = mutableListOf<ArticleInfo>()

    override fun initView() {
        setupPageState(v.recyclerView)
        adapter = SearchAdapter(this, datas)
        v.recyclerView.layoutManager = LinearLayoutManager(this)
        v.recyclerView.adapter = adapter
        v.recyclerView.addItemDecoration(ItemDecorationWidget(0, Utils.getRealPixel(1)).apply {
            setColor(0xffdddddd.toInt())
        })
    }

    override fun initListener() {
        adapter?.itemClick { _, articleInfo ->
            WebViewActivity.start(this, articleInfo.link)
        }

        v.searchView.onSearch = {
            vm.search(it, 0)
        }

        v.searchView.onBack = {
            onBackPressed()
        }

        v.searchView.onCancel = {
            if (it) {
                vm.searchResult.value = mutableListOf()
            } else {
                finishAfterTransition()
            }
        }
    }

    override fun initData(bundle: Bundle?) {
    }

    override fun initVM() {
        vm.searchResult.observe(this, {
            datas.clear()
            datas.addAll(it)
            v.recyclerView.adapter?.notifyDataSetChanged()
        })
    }
}