package cn.lgh.openapp.ui.main.home.adapter

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import cn.lgh.openapp.bean.BannerInfo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.youth.banner.adapter.BannerAdapter

/**
 * @author lgh
 * @date 2020/10/9
 *
 */
class HomeBannerAdapter(private val datas: MutableList<BannerInfo>) :
    BannerAdapter<BannerInfo, HomeBannerViewHolder>(datas) {

    private var context: Context? = null

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): HomeBannerViewHolder {
        context = parent?.context
        val imageView = ImageView(parent?.context)
        imageView.layoutParams = ViewGroup.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        return HomeBannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: HomeBannerViewHolder?,
        data: BannerInfo?,
        position: Int,
        size: Int
    ) {
        holder?.let {
            Glide.with(it.itemView)
                .load(data?.imagePath)
//                .apply(RequestOptions.bitmapTransform(RoundedCorners(30)))
                .into(holder.imageView)
        }
    }
}

class HomeBannerViewHolder(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)