package cn.lgh.openapp.bean

/**
 * @author lgh
 * @date 2020/9/27
 *
 */
open class BaseResult<T>(
    val errorCode: Int = -1,
    val errorMsg: String? = null,
    var data: T? = null
)