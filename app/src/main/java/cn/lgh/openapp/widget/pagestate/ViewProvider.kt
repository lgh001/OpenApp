package cn.lgh.openapp.widget.pagestate

import android.content.Context
import android.view.View

/**
 * @author lgh
 * @date 2020/10/14
 *
 */
interface LoadStateViewCreator {

//    companion object {
//        @Volatile
//        var instance: ViewProvider? = null
//            get() {
//                return field ?: synchronized(this) {
//                    field ?: ViewProvider().also { field = it }
//                }
//            }
//    }

    fun createView(context: Context): ILoadView
}

interface EmptyViewCreator {

    fun createView(context: Context): IView
}


interface ILoadView : IView {

    fun startLoading()

    fun stopLoading()
}

interface IView {
    fun getView(): View
    var onClick:((View?)->Unit)?
}