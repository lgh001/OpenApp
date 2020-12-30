package cn.lgh.openapp.ui.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import cn.lgh.openapp.event.EventCode
import cn.lgh.openapp.event.EventMassage
import cn.lgh.openapp.http.error.ErrorResult
import cn.lgh.openapp.widget.pagestate.PageStateLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.reflect.ParameterizedType

/**
 * @author lgh
 * @date 2020/9/27
 *
 */
abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment(), IProcess, IRefresh {

    lateinit var mContext: Context
    var contentView: View? = null
    lateinit var vm: VM
    lateinit var v: VB

    var mRefreshLayout: SmartRefreshLayout? = null

    /**
     * 用来控制和显示页面状态的view
     */
    private lateinit var mPageState: PageStateLayout

    private var isViewCreated = false
    lateinit var delegate: IProcess

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
            mPageState = PageStateLayout(inflater.context)
            mPageState.onClick = {
                onReLoad(it)
            }
            delegate = PageDelegate(inflater.context, mRefreshLayout, mPageState, this, vm)
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
    }

    private fun init() {
        EventBus.getDefault().register(this)
        performVM(this, vm)
    }

    override fun errorResult(errorResult: ErrorResult) {
        delegate.errorResult(errorResult)
    }

    override fun showLoading() {
        delegate.showLoading()
    }

    override fun hideLoading() {
        delegate.hideLoading()
    }

    override fun loadFinish() {
        delegate.loadFinish()
    }


    override fun noMore(showStatus: Boolean) {
        delegate.noMore(showStatus)
    }

    override fun reset() {
        delegate.reset()
    }

    override fun exceptionResult(e: Throwable?) {
        delegate.exceptionResult(e)
    }

    override fun pageStatus(status: PageStateLayout.Status) {
        delegate.pageStatus(status)
    }

    override fun performVM(owner: LifecycleOwner, vm: BaseViewModel) {
        delegate.performVM(owner, vm)
    }

    open fun onReLoad(state: PageStateLayout.Status) {}

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun handleEvent(msg: EventMassage) {
        when (msg.code) {
            EventCode.LOGIN_SUCCESS -> { }
        }
    }

    abstract fun initView()
    abstract fun initListener()
    abstract fun initData()
    abstract fun initVM()

    abstract fun lazyLoadData()

    /**
     * 懒加载实现方式
     * 1.如果是用旧版本的Viewpager+Fragment方式，需要在setUserVisibleHint()方法里面设置，当页面可见，会回调
     *   isVisibleToUser=true,不可见isVisibleToUser=false
     * 2.如果使用FragmentStatePagerAdapter，并且设置了Behavior=BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT.
     *   那么，setUserVisibleHint将不会回调，而是通过FragmentTransaction.setMaxLifecycle()来控制，
     *   简单点来说就是，当Behavior=BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT时，可见的Fragment生命周期会
     *   走到onResume(相当于isVisibleToUser=true),不可见的时候，生命周期走到onStart(相当于isVisibleToUser=false)
     *   具体源码看{@link FragmentStatePagerAdapter#instantiateItem}
     *   所以这种方式实现懒加载非常简单，只需要在onResume的时候回调懒加载方法，并且加个变量保证懒加载只执行一次
     * 总体来说，更推荐方式2,实现更简单
     */
    private fun lazyLoad() {
        if (isViewCreated) {
            lazyLoadData()
            isViewCreated = false
        }
    }

    override fun hasRefresh(): Boolean = false

    override fun onRefresh() {
        vm.isRefresh.value = true
    }

    override fun onLoadMore() {
        vm.isRefresh.value = false
    }

    override fun autoRefresh(delay: Int?) {
        delegate.autoRefresh(delay)
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

    fun setupPageState(contentView:View){
        mPageState.setupContentView(contentView)
    }


    override fun onResume() {
        super.onResume()
        lazyLoad()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        contentView = null
        isViewCreated = false
        EventBus.getDefault().unregister(this)
    }
}