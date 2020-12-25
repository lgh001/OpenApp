package cn.lgh.openapp.ui.main.knowledgetree

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.lgh.openapp.bean.Leaf
import cn.lgh.openapp.databinding.FragmentKnowledgeTreeBinding
import cn.lgh.openapp.ui.base.BaseFragment
import cn.lgh.openapp.ui.main.articlelist.ArticleListActivity
import cn.lgh.openapp.ui.main.knowledgetree.adapter.KnowledgeTreeAdapter
import cn.lgh.openapp.ui.main.knowledgetree.vm.KnowledgeTreeViewModel
import cn.lgh.openapp.utils.Utils
import cn.lgh.openapp.widget.ItemDecorationWidget
import cn.lgh.openapp.widget.startActivity

/**
 * @author lgh
 * @date 2020/9/30
 *
 */
class KnowledgeTreeFragment : BaseFragment<KnowledgeTreeViewModel, FragmentKnowledgeTreeBinding>() {

    private val mKnowledgeTreeData = mutableListOf<Leaf>()
    private var mAdapter: KnowledgeTreeAdapter? = null

    override fun initView() {
        val lm = LinearLayoutManager(context)
        v.recyclerView.layoutManager = lm
        v.recyclerView.addItemDecoration(
            ItemDecorationWidget(0, Utils.getRealPixel(20))
        )
        mAdapter = KnowledgeTreeAdapter(mKnowledgeTreeData)
        v.recyclerView.adapter = mAdapter
    }

    override fun initListener() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            startActivity<ArticleListActivity>(
                Pair(
                    ArticleListActivity.CATE_ID,
                    mKnowledgeTreeData[position].children[0].id
                ),
                Pair(
                    ArticleListActivity.CATE_NAME,
                    mKnowledgeTreeData[position].children[0].name
                )
            )
        }

        mAdapter?.tagClickListener = {
            startActivity<ArticleListActivity>(
                Pair(ArticleListActivity.CATE_ID, it.id),
                Pair(ArticleListActivity.CATE_NAME, it.name)
            )
        }
    }

    override fun initData() {}

    override fun initVM() {
        vm.knowledgeTree.observe(this, Observer {
            mKnowledgeTreeData.addAll(it)
            v.recyclerView.adapter?.notifyDataSetChanged()
        })
    }

    override fun lazyLoadData() {
        vm.getTree()
    }
}