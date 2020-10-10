package cn.lgh.openapp.widget

import androidx.lifecycle.viewModelScope
import cn.lgh.openapp.bean.BaseResult
import cn.lgh.openapp.http.error.ErrorResult
import cn.lgh.openapp.http.error.ErrorUtil
import cn.lgh.openapp.ui.base.BaseViewModel
import kotlinx.coroutines.*

/**
 * @author lgh
 * @date 2020/9/27
 *
 */

suspend fun <T> BaseViewModel.request(
    isShowLoading: Boolean = false,
    isShowError: Boolean = true,
    block: suspend () -> BaseResult<T>
): BaseResult<T>? {
    if (isShowLoading) {
        this.showLoading()
    }

    val result = viewModelScope.async(start = CoroutineStart.LAZY, context = Dispatchers.IO) {
        block()
    }
    try {

        val res = result.await()
        hideLoading()
        loadFinish()
        if (res.errorCode != 0) {
            //出错
            handleError(res, this, isShowError)
            return null
        }
        return res
    } catch (e: Exception) {
        this.errorData.value = ErrorUtil.getError(e).also {
            it.show = isShowError
        }
    }
    return null
}

fun <T : BaseResult<*>> handleError(t: T?, viewModel: BaseViewModel, isShowError: Boolean) {
    if (t == null) {
        viewModel.errorData.value = ErrorUtil.getError(Exception()).also {
            it.show = isShowError
        }
    }
    when (t?.errorCode) {
        205 -> {
            //token过期
        }
        else -> {
            val data = ErrorResult()
            data.errMsg = t?.errorMsg
            data.code = t?.errorCode!!
            viewModel.errorData.value = data
        }
    }
}