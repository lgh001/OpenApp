package cn.lgh.openapp.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.lgh.openapp.http.HttpUtil
import cn.lgh.openapp.http.error.ErrorResult

/**
 * @author lgh
 * @date 2020/9/27
 *
 */
open class BaseViewModel : ViewModel() {


    val repo by lazy {
        HttpUtil.getInstance().getService()
    }

    var isShowLoading = MutableLiveData<Boolean>()//是否显示loading
    var errorData = MutableLiveData<ErrorResult>()
    var isLoadFinish = MutableLiveData<Boolean>()

    fun showLoading() {
        isShowLoading.value = true
        isLoadFinish.value = false
    }

    fun hideLoading() {
        isShowLoading.value = false
    }

    fun loadFinish() {
        isLoadFinish.value = true
    }

}