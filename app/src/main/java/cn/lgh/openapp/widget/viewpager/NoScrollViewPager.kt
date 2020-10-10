package cn.lgh.openapp.widget.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * @author lgh
 * @date 2020/9/30
 *
 */
class NoScrollViewPager : ViewPager {

    private var noScroll=false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun setNoScroll(noScroll:Boolean){
        this.noScroll=noScroll
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        if (noScroll) return false
        return super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (noScroll) return false
        return super.onInterceptTouchEvent(ev)
    }
}