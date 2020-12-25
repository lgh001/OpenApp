package cn.lgh.openapp.bean

/**
 * @author lgh
 * @date 2020/12/17
 *
 */
class WXAuthor(
    val children: List<WXAuthor>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)