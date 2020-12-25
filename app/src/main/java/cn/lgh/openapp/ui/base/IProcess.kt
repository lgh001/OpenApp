package cn.lgh.openapp.ui.base

import androidx.lifecycle.LifecycleOwner
import cn.lgh.openapp.http.error.ErrorResult
import cn.lgh.openapp.widget.pagestate.PageStateLayout

/**
 * @author lgh
 * @date 2020/12/4
 *
 */
interface IProcess {
    /**
     * 显示loading
     */
    fun showLoading()

    /**
     * 隐藏loading
     */
    fun hideLoading()

    /**
     * 加载完成
     */
    fun loadFinish()

    /**
     * 请求没有更多
     * @param showStatus Boolean  是否第一次请求，如果是，那么说明页面数据为空，一般需要显示空状态
     */
    fun noMore(showStatus: Boolean)

    /**
     * 重置所有状态
     */
    fun reset()

    /**
     * 请求返回错误
     * @param err ResultBase? 具体错误
     */
    fun errorResult(errorResult: ErrorResult)

    /**
     * 请求异常，比如说断网等客户端原因找出的请求异常
     * @param e Throwable? 异常
     */
    fun exceptionResult(e:Throwable?)

    /**
     * 页面状态
     * @param status Status 状态
     */
    fun pageStatus(status: PageStateLayout.Status)


    /**
     * 执行vm
     * @param owner LifecycleOwner
     * @param vm BaseViewModel
     */
    fun performVM(owner:LifecycleOwner,vm:BaseViewModel)
}