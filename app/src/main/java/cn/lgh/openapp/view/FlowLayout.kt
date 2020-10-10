package cn.lgh.openapp.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup

/**
 * @author lgh
 * @date 2020/10/10
 *
 */
class FlowLayout : ViewGroup {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleRes: Int = 0) : super(
        context,
        attrs,
        defStyleRes
    )

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //首先测量所有子view
        //测量子view才能知道子view的宽高
        measureChildren(widthMeasureSpec,heightMeasureSpec)


    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0..childCount){
            val child=getChildAt(i)
            val rect=child.tag as Rect
            child.layout(rect.left,rect.top,rect.right,rect.bottom)
        }
    }
}