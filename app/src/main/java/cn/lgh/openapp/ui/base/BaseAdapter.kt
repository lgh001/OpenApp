package cn.lgh.openapp.ui.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import cn.lgh.openapp.widget.clicks
import java.lang.reflect.ParameterizedType

/**
 * @author lgh
 * @date 2020/9/27
 * 支持viewBinding的adapter的基类
 */
abstract class BaseAdapter<VB : ViewBinding, T>(
    var mContext: Context,
    var listDatas: List<T>
) : RecyclerView.Adapter<BaseViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz = type.actualTypeArguments[0] as Class<VB>
        val method = clazz.getMethod("inflate", LayoutInflater::class.java)
        val vb = method.invoke(null, LayoutInflater.from(mContext)) as VB
        vb.root.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        return BaseViewHolder(vb, vb.root)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.itemView.clicks {
            itemClick?.invoke(position, listDatas[position])
        }
        holder.itemView.setOnLongClickListener {
            itemLongClick?.invoke(position, listDatas[position])
            true
        }
        convert(holder.v as VB, listDatas[position], position)
    }

    abstract fun convert(v: VB, item: T, position: Int)

    override fun getItemCount(): Int {
        return listDatas.size
    }

    private var itemClick: ((Int, T) -> Unit)? = null
    private var itemLongClick: ((Int, T) -> Unit)? = null

    fun itemClick(itemClick: (Int, T) -> Unit) {
        this.itemClick = itemClick
    }

    fun itemLongClick(itemLongClick: (Int, T) -> Unit) {
        this.itemLongClick = itemLongClick
    }
}