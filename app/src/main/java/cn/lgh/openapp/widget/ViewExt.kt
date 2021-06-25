package cn.lgh.openapp.widget

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.ViewGroupUtils

/**
 * @author lgh
 * @date 2021/6/24
 *
 */

@SuppressLint("RestrictedApi")
fun View.expand(dx: Int, dy: Int) {

    class MultiTouchDelegate(bound: Rect? = null, delegateView: View) :
        TouchDelegate(bound, delegateView) {
        val delegateViewMap = mutableMapOf<View, Rect>()
        private var delegateView: View? = null

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val x = event.x.toInt()
            val y = event.y.toInt()
            var handled = false
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    delegateView = findDelegateViewUnder(x, y)
                }
                MotionEvent.ACTION_CANCEL -> {
                    delegateView = null
                }
            }
            delegateView?.let {
                event.setLocation(it.width / 2f, it.height / 2f)
                handled = it.dispatchTouchEvent(event)
            }
            return handled
        }

        private fun findDelegateViewUnder(x: Int, y: Int): View? {
            delegateViewMap.forEach { entry -> if (entry.value.contains(x, y)) return entry.key }
            return null
        }
    }

    val parentView = parent as? ViewGroup
    //如果父view不是ViewGroup,直接返回
    parentView ?: return

    if (parentView.touchDelegate == null) parentView.touchDelegate =
        MultiTouchDelegate(delegateView = this)
    post {
        val rect = Rect()
        //获取子控件在父控件的区域
        ViewGroupUtils.getDescendantRect(parentView, this, rect)
        //将响应区域扩大
        rect.inset(-dx, -dy)
        (parentView.touchDelegate as? MultiTouchDelegate)?.delegateViewMap?.put(this, rect)
    }

}