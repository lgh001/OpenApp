package cn.lgh.openapp.bean

/**
 * @author lgh
 * @date 2020/10/10
 *
 */
data class Leaf(
    val children: List<Leaf>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)
