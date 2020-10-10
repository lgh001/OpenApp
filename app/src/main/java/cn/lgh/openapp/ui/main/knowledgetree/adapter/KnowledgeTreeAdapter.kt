package cn.lgh.openapp.ui.main.knowledgetree.adapter

import android.view.View
import cn.lgh.openapp.R
import cn.lgh.openapp.bean.Leaf
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_knowledge_tree.view.*

/**
 * @author lgh
 * @date 2020/10/10
 *
 */
class KnowledgeTreeAdapter(datas: MutableList<Leaf>) :
    BaseQuickAdapter<Leaf, KnowledgeTreeViewHolder>(
        R.layout.item_knowledge_tree, datas
    ) {

    val buffer = StringBuilder()

    override fun convert(holder: KnowledgeTreeViewHolder, item: Leaf) {
        holder.item.title.text = item.name
        buffer.clear()
        item.children.forEach { leaf ->
            buffer.append(leaf.name)
            buffer.append("  ")
        }
        holder.item.desc.text = buffer
    }
}

class KnowledgeTreeViewHolder(val item: View) : BaseViewHolder(item)