package cn.lgh.openapp.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.EditText
import cn.lgh.openapp.R

/**
 * @author lgh
 * @date 2021/6/28
 * 可以清除输入的输入框
 */
class ClearEditText @JvmOverloads constructor(
    context: Context, val attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : EditText(context, attrs, defStyleAttr) {

    private var clearDrawable: Drawable? = null
    private var searchDrawable: Drawable? = null

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText)

        clearDrawable = a.getDrawable(R.styleable.ClearEditText_clearIcon)
        searchDrawable = a.getDrawable(R.styleable.ClearEditText_searchIcon)
        setCompoundDrawablesWithIntrinsicBounds(searchDrawable, null, null, null)
        a.recycle()
    }

    private fun setClearIconVisible(visible: Boolean) {
        setCompoundDrawablesWithIntrinsicBounds(
            searchDrawable,
            null,
            if (visible) clearDrawable else null,
            null
        )
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        setClearIconVisible(hasFocus() && text?.isNotEmpty() == true)
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        setClearIconVisible(focused && length() > 0)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            //当手指抬起的区域在清除按钮区域内，则认为是清除
            MotionEvent.ACTION_UP -> {
                if (clearDrawable != null && event.x <= (width - paddingRight)
                    && event.x >= (width - paddingRight - clearDrawable?.bounds?.width()!!)
                ){
                    setText("")
                }
            }
        }
        return super.onTouchEvent(event)
    }

}