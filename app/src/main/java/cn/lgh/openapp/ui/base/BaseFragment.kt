package cn.lgh.openapp.ui.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import cn.lgh.openapp.R
import cn.lgh.openapp.event.EventCode
import cn.lgh.openapp.event.EventMassage
import cn.lgh.openapp.http.error.ErrorResult
import cn.lgh.openapp.widget.toast
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.reflect.ParameterizedType

/**
 * @author lgh
 * @date 2020/9/27
 *
 */
abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment() {

    lateinit var mContext: Context
    var contentView: View? = null
    lateinit var vm: VM
    lateinit var v: VB

    var mRefreshLayout: SmartRefreshLayout? = null

    private var isViewCreated = false
    private var isUIVisible = true
    var isVisibleToUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = javaClass.genericSuperclass as ParameterizedType
        val clz1 = type.actualTypeArguments[0] as Class<VM>
        vm = ViewModelProvider(this).get(clz1)

        val clz2 = type.actualTypeArguments[1] as Class<VB>
        val method = clz2.getMethod("inflate", LayoutInflater::class.java)
        v = method.invoke(null, layoutInflater) as VB

        mContext = context!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null == contentView) {
            contentView = if (hasRefresh()) setupRefreshLayout(v.root) else v.root
            init()
            initView()
            initListener()
            initData()
            initVM()
        }
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        lazyLoad()
    }

    private fun init() {
        EventBus.getDefault().register(this)
        vm.isShowLoading.observe(this, Observer {
            if (it) showLoading() else hideLoading()
        })

        vm.errorData.observe(this, Observer {
            if (it.show) toast(it.errMsg)
            errorResult(it)
        })

        vm.isLoadFinish.observe(this, Observer {
            if (it) {
                loadFinish()
            }
        })
    }

    open fun errorResult(errorResult: ErrorResult) {}

    fun showLoading() {
    }

    fun hideLoading() {
    }

    fun loadFinish() {
        mRefreshLayout?.finishRefresh()
        mRefreshLayout?.finishLoadMore()
    }

    @Subscribe
    open fun handleEvent(msg: EventMassage) {
        when (msg.code) {
            EventCode.LOGIN_SUCCESS -> {
            }
        }
    }

    abstract fun initView()
    abstract fun initListener()
    abstract fun initData()
    abstract fun initVM()

    abstract fun lazyLoadData()

    private fun lazyLoad() {
        if (isViewCreated && isUIVisible) {
            lazyLoadData()
            isViewCreated = false
            isUIVisible = false
        }
    }

    open fun hasRefresh(): Boolean = false

    open fun onRefresh() {

    }

    open fun onLoadMore() {

    }

    private fun setupRefreshLayout(view: View): View? {
        if (mRefreshLayout == null) {
            mRefreshLayout = SmartRefreshLayout(mContext)
        }
        return mRefreshLayout?.also {
            it.addView(
                view,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            it.setOnRefreshListener {
                onRefresh()
            }
            it.setOnLoadMoreListener {
                onLoadMore()
            }
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        if (isVisibleToUser) {
            isUIVisible = true
            lazyLoad()
        } else {
            isUIVisible = false
        }
    }

    override fun onResume() {
        super.onResume()
        lazyLoad()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        contentView = null
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().register(this)
    }
}