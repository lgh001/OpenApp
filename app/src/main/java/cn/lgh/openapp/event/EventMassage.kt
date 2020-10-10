package cn.lgh.openapp.event

/**
 * @author lgh
 * @date 2020/9/27
 *
 */
class EventMassage @JvmOverloads constructor(
    var code: EventCode,
    var msg: String? = "",
    var arg1: Int = 0,
    var arg2: Int = 0,
    var obj: Any? = null
)