package cn.lgh.openapp.ui.main.common.adapter

import android.content.Context
import android.text.TextUtils
import cn.lgh.openapp.R
import cn.lgh.openapp.bean.WXArticleInfo
import cn.lgh.openapp.databinding.ItemArticleBinding
import cn.lgh.openapp.ui.base.BaseAdapter

/**
 * @author lgh
 * @date 2020/12/17
 * 微信公众号作品adapter
 */
class WXArticlesAdapter(context: Context, list: List<WXArticleInfo>) :
    BaseAdapter<ItemArticleBinding, WXArticleInfo>(context, list) {

    override fun convert(v: ItemArticleBinding, item: WXArticleInfo, position: Int) {
        v.articleTitle.text = item.title
        v.authorName.text = if (TextUtils.isEmpty(item.author)) item.shareUser else item.author
        v.cateName.text = item.chapterName
        v.timeName.text = item.niceDate
        v.articleFavorite.setImageResource(if (item.collect) R.mipmap.ic_favorite_light else R.mipmap.ic_favorite_nomal)

        v.articleFavorite.setOnClickListener {
            onFavoriteClickListener?.invoke(item)
        }
    }

    var onFavoriteClickListener: ((WXArticleInfo) -> Unit)? = null
}