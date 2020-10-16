package cn.lgh.openapp.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import cn.lgh.openapp.event.EventCode
import cn.lgh.openapp.event.EventMassage
import cn.lgh.openapp.http.error.ErrorResult
import cn.lgh.openapp.widget.pagestate.PageStateLayout
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

        vm.noMore.observe(this, Observer {
            if (it) {
                noMore()
            }
        })

        vm.isFirstPage.observe(this, Observer {

        })

        vm.loadSuccess.observe(this, Observer {

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


    fun noMore() {
        mRefreshLayout?.finishLoadMoreWithNoMoreData()
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


    override fun onResume() {
        super.onResume()
        lazyLoad()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        contentView = null
        isViewCreated = false
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().register(this)
    }
}