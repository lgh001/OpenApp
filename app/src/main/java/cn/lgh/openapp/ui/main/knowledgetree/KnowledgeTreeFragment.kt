package cn.lgh.openapp.ui.main.knowledgetree

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.lgh.openapp.bean.Leaf
import cn.lgh.openapp.databinding.FragmentKnowledgeTreeBinding
import cn.lgh.openapp.ui.base.BaseFragment
import cn.lgh.openapp.ui.main.knowledgetree.adapter.KnowledgeTreeAdapter
import cn.lgh.openapp.ui.main.knowledgetree.vm.KnowledgeTreeViewModel
import cn.lgh.openapp.utils.Utils
import cn.lgh.openapp.widget.ItemDecorationWidget
import cn.lgh.openapp.widget.toast

/**
 * @author lgh
 * @date 2020/9/30
 *
 */
class KnowledgeTreeFragment : BaseFragment<KnowledgeTreeViewModel, FragmentKnowledgeTreeBinding>() {

    private val mKnowledgeTreeData = mutableListOf<Leaf>()
    private var mAdapter: KnowledgeTreeAdapter? = null

    override fun initView() {
        v.recyclerView.layoutManager = LinearLayoutManager(context)
        v.recyclerView.addItemDecoration(
            ItemDecorationWidget(0, Utils.getRealPixel(20))
        )
        mAdapter = KnowledgeTreeAdapter(mKnowledgeTreeData)
        v.recyclerView.adapter = mAdapter
    }

    override fun initListener() {
        mAdapter?.setOnItemClickListener { adapter, view, position ->
            toast(mKnowledgeTreeData[position].name)
        }
    }

    override fun initData() {
    }

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