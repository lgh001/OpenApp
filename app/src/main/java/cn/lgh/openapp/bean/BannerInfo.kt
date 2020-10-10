package cn.lgh.openapp.bean

/**
 * @author lgh
 * @date 2020/10/9
 *
 */
data class BannerInfo(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)