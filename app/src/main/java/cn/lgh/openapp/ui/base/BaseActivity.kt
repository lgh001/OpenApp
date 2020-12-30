package cn.lgh.openapp.ui.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import cn.lgh.openapp.R
import cn.lgh.openapp.event.EventCode
import cn.lgh.openapp.event.EventMassage
import cn.lgh.openapp.http.error.ErrorResult
import cn.lgh.openapp.widget.pagestate.PageStateLayout
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.reflect.ParameterizedType

/**
 * @author lgh
 * @date 2020/9/27
 * activity基类  页面尽可能继承此类
 */
abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity(), IProcess,
    IRefresh {

    lateinit var mContext: Context
    lateinit var vm: VM
    lateinit var v: VB


    var mRefreshLayout: SmartRefreshLayout? = null

    /**
     * 用来控制和显示页面状态的view
     */
    private lateinit var mPageState: PageStateLayout

    lateinit var delegate: IProcess

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initResources()

        //type.actualTypeArguments[0]=BaseViewModel，type.actualTypeArguments[1]=ViewBinding
        //主要根据类的泛型，BaseActivity<VM:BaseViewModel,VB:ViewBinding>
        //可以看到第一个是 VM:BaseViewModel
        val type = javaClass.genericSuperclass as ParameterizedType
        val clz1 = type.actualTypeArguments[0] as Class<VM>
        vm = ViewModelProvider(this).get(clz1)

        val clz2 = type.actualTypeArguments[1] as Class<VB>
        //通过反射获取vb
        val method = clz2.getMethod("inflate", LayoutInflater::class.java)
        v = method.invoke(null, layoutInflater) as VB
        val view = if (hasRefresh()) setupRefreshLayout(v.root) else v.root
        setContentView(view)
        mContext = this
        ImmersionBar.with(this)
            .transparentBar()
            .statusBarColor(R.color.app_main_color)
            .titleBarMarginTop(view)
            .autoDarkModeEnable(true)
            .init()

        mPageState = PageStateLayout(this)
        mPageState.onClick = {
            onReLoad(it)
        }
        delegate = PageDelegate(this, mRefreshLayout, mPageState, this, vm)
        init()
        preData()
        initView()
        initListener()
        initData(savedInstanceState)
        initVM()
    }

    /**
     * 防止系统字体影响到app的字体
     */
    open fun initResources() {
        val config = Configuration()
        config.setToDefaults()
        createConfigurationContext(config)
    }

    open fun preData() {}
    abstract fun initView()
    abstract fun initListener()
    abstract fun initData(bundle: Bundle?)
    abstract fun initVM()


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
            mRefreshLayout = SmartRefreshLayout(this)
        }
        return mRefreshLayout?.also {
            it.addView(view)
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

    fun loadRootMultiFragment(id: Int, show: Int, vararg fragment: Fragment) {
        val fm = supportFragmentManager
        val t = fm.beginTransaction()
        for (i in fragment.indices) {
            val to = fragment[i]
            val name = to::class.java.name
            t.add(id, to, name)
            if (i != show) {
                t.hide(to)
            }
        }
        t.commitAllowingStateLoss()
    }

    fun showAndHideFragment(show: Fragment, hide: Fragment) {
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.show(show)
        ft.hide(hide)
        ft.commitAllowingStateLoss()
    }

    @Subscribe
    open fun handleEvent(msg: EventMassage) {
        when (msg.code) {
            EventCode.LOGIN_SUCCESS -> { }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}