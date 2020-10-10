package cn.lgh.openapp.http.error

/**
 * @author lgh
 * @date 2020/9/28
 *
 */
data class ErrorResult(
    var code:Int=0,
    var errMsg :String?="",
    var show:Boolean=false,
    var index:Int=0//表示api类型(确定是哪个api)
)