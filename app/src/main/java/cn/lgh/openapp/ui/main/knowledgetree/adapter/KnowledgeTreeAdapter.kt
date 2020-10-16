package cn.lgh.openapp.ui.main.knowledgetree.adapter

import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import cn.lgh.openapp.R
import cn.lgh.openapp.bean.Leaf
import cn.lgh.openapp.utils.Utils
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

    var tagClickListener: ((Leaf) -> Unit)? = null

    override fun convert(holder: KnowledgeTreeViewHolder, item: Leaf) {
        holder.item.title.text = item.name
        val layoutParams =
            LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Utils.getRealPixel(60))
        layoutParams.setMargins(
            Utils.getRealPixel(10),
            Utils.getRealPixel(5),
            Utils.getRealPixel(10),
            Utils.getRealPixel(5)
        )
        holder.item.flow_layout.removeAllViews()
        item.children.forEach { l ->
            val tag = TextView(holder.item.context)
            tag.setBackgroundResource(R.drawable.bg_tag)
            tag.text = l.name
            tag.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15f)
            tag.setTextColor(0xff333333.toInt())
            tag.gravity = Gravity.CENTER
            tag.setPadding(
                Utils.getRealPixel(20),
                Utils.getRealPixel(5),
                Utils.getRealPixel(20),
                Utils.getRealPixel(5)
            )
            tag.setOnClickListener {
                tagClickListener?.invoke(l)
            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                tag.foreground=holder.item.context.getDrawable(R.drawable.ripple)
//            }
            tag.layoutParams = layoutParams
            holder.item.flow_layout.addView(tag)
        }
    }
}

class KnowledgeTreeViewHolder(val item: View) : BaseViewHolder(item)