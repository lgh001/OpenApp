package cn.lgh.openapp.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author lgh
 * @date 2020/9/27
 *
 */
open class BaseViewHolder(var v: ViewBinding,itemView:View) : RecyclerView.ViewHolder(itemView)