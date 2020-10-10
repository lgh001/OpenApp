package cn.lgh.openapp

import android.app.Application
import cn.lgh.openapp.utils.Utils
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

    override fun onCreate() {
        super.onCreate()
        Utils.init(this)

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            MaterialHeader(context).apply {
                setColorSchemeColors(0xff5A88EA.toInt())
            }
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context).apply {
                setAccentColor(0xff5A88EA.toInt())
            }
        }

    }

}