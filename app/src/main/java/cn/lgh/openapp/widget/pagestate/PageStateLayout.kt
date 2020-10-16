package cn.lgh.openapp.widget.pagestate

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import java.lang.RuntimeException

/**
 * @author lgh
 * @date 2020/10/14
 * 页面状态
 */
class PageStateLayout : FrameLayout {

    enum class Status {
        NORMAL, LOADING, EMPTY, ERROR
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object {

        var sLoadViewCreator: LoadStateViewCreator? = null
        var sEmptyViewCreator: EmptyViewCreator? = null
        var sErrorViewCreator: EmptyViewCreator? = null

    }

    /**
     * loading中的view
     */
    private var mLoadView: ILoadView? = null

    /**
     *
     */
    private var mEmptyView: IView? = null
    private var mErrorView: IView? = null
    var mContentView: View? = null

    /**
     * 当前状态
     */
    var currentStatus: Status = Status.NORMAL

    var onClick: ((Status) -> Unit)? = null

    /**
     * 是否已经设置了内容view
     */
    private var isSetup = false

    init {
//        mEmptyView = sEmptyViewCreator?.createView(context)
//        mErrorView = sErrorViewCreator?.createView(context)
//        mLoadView = sLoadViewCreator?.createView(context)
    }


    /**
     * 设置内容view,状态显示区域，
     * 例如：当前的内容是一个列表，那么就把recyclerView传进来
     * @param contentView 内容view
     */
    fun setupContentView(contentView: View) {
        val parent = (contentView.parent as ViewGroup) ?: throw RuntimeException("内容view必须有父类")
        var contentLayoutParams = contentView.layoutParams
        contentLayoutParams = contentLayoutParams
            ?: ViewGroup.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )

        parent.removeView(contentView)
        mContentView = contentView
        parent.addView(this, contentLayoutParams)
        isSetup = true
        setState(Status.NORMAL)
    }

    /**
     * 设置当前的状态
     * @param state 状态
     */
    fun setState(state: Status) {
        if (!isSetup) {
            return
        }
        currentStatus = state
        when (state) {
            Status.NORMAL -> {
                stopLoading()
                this.removeAllViews()
                val fp =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                this.addView(mContentView, fp)
            }
            Status.LOADING -> {
                this.removeAllViews()
                val fp =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                fp.gravity = Gravity.CENTER
                this.addView(getLoadingView()?.getView(), fp)
                startLoading()
            }
            Status.EMPTY -> {
                stopLoading()
                this.removeAllViews()
                val fp =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                fp.gravity = Gravity.CENTER
                this.addView(getEmptyView()?.getView(), fp)
            }
            Status.ERROR -> {
                stopLoading()
                this.removeAllViews()
                val fp =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                fp.gravity = Gravity.CENTER
                this.addView(getErrorView()?.getView(), fp)
            }
            else -> {
            }
        }
    }

    private fun startLoading() {
        val view = getLoadingView()
        view?.startLoading()
    }

    private fun stopLoading() {
        val view = getLoadingView()
        view?.stopLoading()
    }

    private fun getLoadingView(): ILoadView? {
        mLoadView = mLoadView ?: sLoadViewCreator?.createView(context)
        return mLoadView
    }

    private fun getEmptyView(): IView? {
        mEmptyView = mEmptyView ?: sEmptyViewCreator?.createView(context)?.also {
            onClick?.invoke(currentStatus)
        }
        return mEmptyView
    }

    private fun getErrorView(): IView? {
        mErrorView = mErrorView ?: sErrorViewCreator?.createView(context)?.also {
            onClick?.invoke(currentStatus)
        }
        return mErrorView
    }
}
