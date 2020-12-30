package cn.lgh.openapp.ui.base

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import cn.lgh.openapp.http.error.ErrorResult
import cn.lgh.openapp.widget.pagestate.PageStateLayout
import cn.lgh.openapp.widget.toast
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * @author lgh
 * @date 2020/12/16
 * 基础页面逻辑代理类
 */
class PageDelegate(
    var context: Context?,
    var mRefreshLayout: SmartRefreshLayout? = null,
    var mPageState: PageStateLayout,
    var refresh: IRefresh,
    var vm: BaseViewModel
) : IProcess {

    private var delay = 0

    override fun reset() {
        mRefreshLayout?.finishRefresh()
        mRefreshLayout?.finishLoadMore()
        mRefreshLayout?.resetNoMoreData()
    }

    override fun exceptionResult(e: Throwable?) {
        context?.toast(e?.message)
    }

    override fun pageStatus(status: PageStateLayout.Status) {
        mPageState.setState(status)
    }

    override fun performVM(owner: LifecycleOwner, vm: BaseViewModel) {
        vm.isShowLoading.observe(owner, Observer {
            if (it) showLoading() else hideLoading()
        })
        vm.errorData.observe(owner, Observer {
            if (it.show) context?.toast(it.errMsg)
            errorResult(it)
        })

        vm.isLoadFinish.observe(owner, Observer {
            if (it) {
                loadFinish()
            }
        })

        vm.noMore.observe(owner, Observer {
            if (it) {
                noMore(vm.isFirstPage.value == true || vm.isRefresh.value == true)
            } else {
                reset()
            }
        })

        vm.loadSuccess.observe(owner, Observer {
            if (it) {
                mPageState.setState(PageStateLayout.Status.NORMAL)
            }
        })

        vm.requestRefresh.observe(owner, Observer {
            if (it) {
                if (refresh.hasRefresh()) mRefreshLayout?.autoRefresh(delay) else refresh.onRefresh()
            }
        })
    }

    override fun autoRefresh(delay: Int?) {
        this.delay = delay ?: 0
        vm.requestRefresh.value = true
    }

    override fun errorResult(errorResult: ErrorResult) {
        mPageState.setState(PageStateLayout.Status.ERROR)
    }

    override fun showLoading() {
        if (refresh.hasRefresh()) {
            mPageState.setState(PageStateLayout.Status.LOADING)
        } else {
            showWaitingDialog()
        }
    }

    override fun hideLoading() {
        mPageState.setState(PageStateLayout.Status.NORMAL)
        hideWaitingDialog()
    }

    override fun loadFinish() {
        mRefreshLayout?.finishRefresh()
        mRefreshLayout?.finishLoadMore()
    }

    override fun noMore(showStatus: Boolean) {
        if (showStatus) {
            pageStatus(PageStateLayout.Status.EMPTY)
        } else {
            mRefreshLayout?.finishLoadMoreWithNoMoreData()
        }
    }

    private fun showWaitingDialog() {}
    private fun hideWaitingDialog() {}
}