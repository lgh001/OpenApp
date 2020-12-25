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
class DefaultErrorView : RelativeLayout, IView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {

        val textView = TextView(context)
        textView.text = "出错"
        textView.gravity = Gravity.CENTER
        val fp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        fp.addRule(CENTER_IN_PARENT)
        addView(textView, fp)
        textView.setOnClickListener {
            onClick?.invoke(it)
        }
    }

    override var onClick: ((View?) -> Unit)?=null

    override fun getView(): View = this
}