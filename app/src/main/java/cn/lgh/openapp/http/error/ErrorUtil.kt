package cn.lgh.openapp.http.error

import retrofit2.HttpException

/**
 * @author lgh
 * @date 2020/9/28
 *
 */
object ErrorUtil {

    fun getError(e:Throwable):ErrorResult{
        val errorResult=ErrorResult()
        if (e is HttpException){
            errorResult.code=e.code()
        }
        errorResult.errMsg=e.message
        if (errorResult.errMsg.isNullOrEmpty())
            errorResult.errMsg="网络请求失败，请重试"
        return errorResult
    }

    fun getError(apiIndex:Int,e: Throwable):ErrorResult{
        return getError(e).apply {
            this.index=apiIndex
        }
    }
}