package cn.lgh.openapp.ui.main.common

import android.content.Context
import cn.lgh.openapp.bean.WXAuthor
import cn.lgh.openapp.databinding.ItemWxAuthorBinding
import cn.lgh.openapp.ui.base.BaseActivity
import cn.lgh.openapp.ui.base.BaseAdapter

/**
 * @author lgh
 * @date 2020/12/17
 *
 */
class WXAuthorListAdapter(context: Context, list: List<WXAuthor>) :
    BaseAdapter<ItemWxAuthorBinding, WXAuthor>(context, list) {



    override fun convert(v: ItemWxAuthorBinding, t: WXAuthor, position: Int) {
    }
}