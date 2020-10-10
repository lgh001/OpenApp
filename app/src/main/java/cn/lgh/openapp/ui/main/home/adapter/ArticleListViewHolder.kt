package cn.lgh.openapp.ui.main.home.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.android.synthetic.main.item_article.view.*

/**
 * @author lgh
 * @date 2020/10/9
 *
 */
class ArticleListViewHolder(val item: View) : BaseViewHolder(item) {

    var favorite: ImageView = item.article_favorite
    var title: TextView = item.article_title
    var authorName: TextView = item.author_name
    var cateName: TextView = item.cate_name
    var time: TextView = item.time_name
}