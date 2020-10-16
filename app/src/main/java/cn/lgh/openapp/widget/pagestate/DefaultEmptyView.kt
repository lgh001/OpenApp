package cn.lgh.openapp.widget.pagestate

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView

/**
 * @author lgh
 * @date 2020/10/15
 *
 */
class DefaultEmptyView : FrameLayout, IView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        val textView = TextView(context)
        textView.text = "什么都没有"
        val fp = LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        addView(textView, fp)
    }

    override fun getView(): View = this
}