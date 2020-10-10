package cn.lgh.openapp.ui.base

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginTop
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import cn.lgh.openapp.R
import cn.lgh.openapp.event.EventCode
import cn.lgh.openapp.event.EventMassage
import cn.lgh.openapp.http.error.ErrorResult
import cn.lgh.openapp.utils.StatusBarUtil
import cn.lgh.openapp.widget.toast
import com.gyf.immersionbar.ImmersionBar
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.reflect.ParameterizedType

/**
 * @author lgh
 * @date 2020/9/27
 *
 */
abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {

    lateinit var mContext: Context
    lateinit var vm: VM
    lateinit var v: VB


    var mRefreshLayout: SmartRefreshLayout? = null

    lateinit var dialog: AlertDialog

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

        setContentView(if (hasRefresh()) setupRefreshLayout(v.root) else v.root)
        mContext = this
        ImmersionBar.with(this)
            .transparentBar()
            .statusBarDarkFont(true)
            .init()


        init()
        preData()
        initView()
        initListener()
        initData()
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
    abstract fun initData()
    abstract fun initVM()

    open fun hasRefresh(): Boolean = false

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
    }

    open fun onRefresh() {

    }

    open fun onLoadMore() {}

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

    @Subscribe
    open fun handleEvent(msg: EventMassage) {
        when (msg.code) {
            EventCode.LOGIN_SUCCESS -> {
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}