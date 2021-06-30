package cn.lgh.openapp.ui.search.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import cn.lgh.openapp.R
import cn.lgh.openapp.bean.ArticleInfo
import cn.lgh.openapp.databinding.ItemArticleBinding
import cn.lgh.openapp.databinding.ItemSearchBinding
import cn.lgh.openapp.ui.base.BaseAdapter
import cn.lgh.openapp.widget.highLight
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * @author lgh
 * @date 2021/6/29
 *
 */
class SearchAdapter(context: Context, datas: MutableList<ArticleInfo>) :
    BaseAdapter<ItemSearchBinding, ArticleInfo>(context, datas) {
    override fun convert(v: ItemSearchBinding, item: ArticleInfo, position: Int) {
        v.tvTitle.text = item.title.highLight()
    }
}

