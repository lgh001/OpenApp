package cn.lgh.openapp.widget.popupwindow

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.annotation.IntDef
import kotlin.math.abs

/**
 * @author lgh
 * @date 2020/10/28
 * 自定义popupWindow
 */
class CustomPopupWindow private constructor(){

    private lateinit var mParams: Params
    var mWindow: PopupWindow? = null

//    constructor() : this(Params())

    constructor(params: Params):this() {
        mParams = params
        mWindow = PopupWindow()
        mWindow?.let {
            if (mParams.mAnimStyle != 0) {
                it.animationStyle = mParams.mAnimStyle
            }
            it.contentView = mParams.mView
            it.isTouchable = mParams.mTouchable
            it.isOutsideTouchable = mParams.mOutsideTouchable
            it.width = mParams.mWidth
            it.height = mParams.mHeight
            if (mParams.mTouchDismiss) {
                it.setTouchInterceptor(mTouchListener)
            }
            if (mParams.mBgColor!=0){
                it.setBackgroundDrawable(ColorDrawable(mParams.mBgColor))
            }
        }
    }

    fun show(anchor: View?) {
        if (mParams.mShowType == Params.SHOW_AT_LOCATION) {
            mWindow?.showAtLocation(anchor, mParams.mGravity, mParams.mXOffset, mParams.mYOffset)
        } else {
            mWindow?.showAsDropDown(anchor, mParams.mXOffset, mParams.mYOffset, mParams.mGravity)
        }
        mParams.mHandler?.postDelayed(mAutoRunnable, mParams.mDuration.toLong())
    }

    private val mAutoRunnable = Runnable { dismiss() }

    fun dismiss() {
        mWindow?.dismiss()
        mParams.mHandler?.removeCallbacks(mAutoRunnable)
        mParams.mDismissListener?.invoke()
    }

    fun isShow(): Boolean {
        return mWindow?.isShowing?:false
    }

    private var mDownX = 0f
    private var mDownY = 0f
    private var xDistance = 0f
    private var yDistance = 0f

    @SuppressLint("ClickableViewAccessibility")
    private var mTouchListener = OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                run {
                    yDistance = 0f
                    xDistance = yDistance
                }
                mDownX = event.x
                mDownY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                val curX = event.x
                val curY = event.y
                xDistance = abs(curX - mDownX)
                yDistance = abs(curY - mDownY)
                return@OnTouchListener if (xDistance > yDistance) {
                    //左右
                    if (xDistance > 3.0f) {
                        dismiss()
                    }
                    true
                } else {
                    //上下
                    if (yDistance > 3.0f) {
                        dismiss()
                    }
                    true
                }
            }
        }
        return@OnTouchListener false
    }

    class Builder(private val P: Params = Params()) {

        fun setContentView(view: View): Builder {
            P.mView = view
            return this
        }

        fun showAtLocation(): Builder {
            P.mShowType = Params.SHOW_AT_LOCATION
            return this
        }

        fun showAtDropDown(): Builder {
            P.mShowType = Params.SHOW_AT_DROP_DOWN
            return this
        }

        fun setAnimStyle(style: Int): Builder {
            P.mAnimStyle = style
            return this
        }

        fun setXOffset(offset: Int): Builder {
            P.mXOffset = offset
            return this
        }

        fun setYOffset(offset: Int): Builder {
            P.mYOffset = offset
            return this
        }

        fun setGravity(gravity: Int): Builder {
            P.mGravity = gravity
            return this
        }

        fun setWidth(width: Int): Builder {
            P.mWidth = width
            return this
        }

        fun setHeight(height: Int): Builder {
            P.mHeight = height
            return this
        }

        fun setTouchable(enable: Boolean): Builder {
            P.mTouchable = enable
            return this
        }

        fun setOutsideTouchable(enable: Boolean): Builder {
            P.mOutsideTouchable = enable
            return this
        }


        fun setAutoCancel(handler: Handler?, duration: Int): Builder {
            P.mHandler = handler
            P.mDuration = duration
            return this
        }

        fun showType(@Params.ShowType type: Int): Builder {
            P.mShowType = type
            return this
        }

        fun setBgColor(color: Int): Builder {
            P.mBgColor = color
            return this
        }

        fun touchDismiss(b: Boolean): Builder {
            P.mTouchDismiss = b
            return this
        }

        fun setDismissListener(dismiss: (() -> Unit)?): Builder {
            P.mDismissListener = dismiss
            return this
        }

        fun build(): CustomPopupWindow {
            return CustomPopupWindow(P)
        }

    }
}

class Params {
    companion object {
        const val SHOW_AT_LOCATION = 1
        const val SHOW_AT_DROP_DOWN = 2
    }

    var mWidth = ViewGroup.LayoutParams.WRAP_CONTENT
    var mHeight = ViewGroup.LayoutParams.WRAP_CONTENT
    var mTouchable = true
    var mOutsideTouchable = false
    var mTouchDismiss = false
    var mAnimStyle = 0
    var mBgColor = 0
    var mXOffset = 0
    var mYOffset = 0
    var mGravity = 0
    var mView: View? = null

    @ShowType
    var mShowType = 0

    var mHandler: Handler? = null
    var mDuration = 2000

    var mDismissListener: (() -> Unit)? = null


    @IntDef(*[SHOW_AT_LOCATION, SHOW_AT_DROP_DOWN])
    @Retention(AnnotationRetention.SOURCE)
    annotation class ShowType
}
