package cn.lgh.openapp.widget.pagestate

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.TextView

/**
 * @author lgh
 * @date 2020/10/15
 *
 */
class DefaultLoadingView : RelativeLayout, ILoadView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        val textView = TextView(context)
        textView.text = "加载中..."
        textView.gravity=Gravity.CENTER
        val fp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        fp.addRule(CENTER_IN_PARENT)
        addView(textView, fp)
    }

    override fun startLoading() {
    }

    override fun stopLoading() {
    }

    override fun getView(): View = this
}