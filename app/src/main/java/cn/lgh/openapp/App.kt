package cn.lgh.openapp

import android.app.Application
import android.content.Context
import cn.lgh.openapp.utils.Utils
import cn.lgh.openapp.widget.pagestate.*
import cn.lgh.openapp.widget.webview.WebViewTools
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout


/**
 * @author lgh
 * @date 2020/9/25
 *
 */
class App : Application() {

    companion object {
        var DEBUG = true
    }

//    lateinit var flutterEngine: FlutterEngine

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)
        WebViewTools.initialize(this)

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            MaterialHeader(context).apply {
                setColorSchemeColors(0xff5A88EA.toInt())
            }
//            ClassicsHeader(context).apply {
//                setAccentColor(0xff5A88EA.toInt())
//            }
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context).apply {
                setAccentColor(0xff5A88EA.toInt())
            }
        }

        PageStateLayout.sLoadViewCreator = object : LoadStateViewCreator {
            override fun createView(context: Context): ILoadView {
                return DefaultLoadingView(context)
            }
        }

        PageStateLayout.sEmptyViewCreator = object : EmptyViewCreator {
            override fun createView(context: Context): IView {
                return DefaultEmptyView(context)
            }
        }

        PageStateLayout.sErrorViewCreator = object : EmptyViewCreator {
            override fun createView(context: Context): IView {
                return DefaultErrorView(context)
            }
        }


//        flutterEngine = FlutterEngine(this)
//        flutterEngine.dartExecutor.executeDartEntrypoint(
//            DartExecutor.DartEntrypoint.createDefault()
//        )
//
//        FlutterEngineCache.getInstance()
//            .put("flutter_engine", flutterEngine)
    }

}