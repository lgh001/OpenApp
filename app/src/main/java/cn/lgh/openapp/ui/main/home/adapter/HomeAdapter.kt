package cn.lgh.openapp.ui.main.home.adapter

import cn.lgh.openapp.R
import cn.lgh.openapp.bean.ArticleInfo
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * @author lgh
 * @date 2020/10/9
 *
 */
class HomeAdapter(mData: MutableList<ArticleInfo>?) :
    BaseQuickAdapter<ArticleInfo, ArticleListViewHolder>(R.layout.item_article, mData) {

    var onFavoriteClickListener: ((collect: Boolean) -> Unit)? = null

    override fun convert(holder: ArticleListViewHolder, item: ArticleInfo) {

        holder.title.text = item.title
        holder.authorName.text = item.author
        holder.cateName.text = item.chapterName
        holder.time.text = item.niceDate
        holder.favorite.setImageResource(if (item.collect) R.mipmap.ic_favorite_light else R.mipmap.ic_favorite_nomal)

        holder.favorite.setOnClickListener {
            onFavoriteClickListener?.invoke(item.collect)
        }
    }
}