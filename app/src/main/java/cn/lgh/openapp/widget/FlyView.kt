package cn.lgh.openapp.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.SparseArray
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.graphics.ColorUtils
import androidx.core.util.keyIterator
import androidx.core.util.valueIterator
import cn.lgh.openapp.utils.Utils
import java.lang.IllegalStateException
import kotlin.random.Random

/**
 * @author lgh
 * @date 2021/6/22
 *
 */
class FlyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mLogoTexts = SparseArray<String>()
    private val mEndPoints = SparseArray<PointF>()
    private val mRandomPoints = SparseArray<PointF>()
    private var mTextColor = Color.BLUE
    private val mPaint = Paint()
    private var mWidth = 0
    private var mHeight = 0
    private var mLogoOffset = 0
    private var mTextPadding = 0
    private var mOffsetAnimProgress = 0f
    private var isOffsetAnimEnd = false
    private var mGradientAnim: ValueAnimator? = null
    private var mMatrixTranslate = 0
    private var mLinearGradient: LinearGradient? = null
    private var mGradientMatrix: Matrix? = null

    init {
        mPaint.color = mTextColor
        mPaint.isAntiAlias = true
        mPaint.textSize = Utils.getRealPixel(40).toFloat()
        mPaint.isFakeBoldText = true
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!isOffsetAnimEnd) {
            mPaint.alpha = 255.coerceAtMost((255 * mOffsetAnimProgress).toInt() + 100)
            for (i in 0 until mEndPoints.size()) {
                val end = mEndPoints[i]
                val start = mRandomPoints[i]
                val x = start.x + (end.x - start.x) * mOffsetAnimProgress
                val y = start.y + (end.y - start.y) * mOffsetAnimProgress
                canvas?.drawText(mLogoTexts[i], x, y, mPaint)
            }
        } else {
            for (i in 0 until mEndPoints.size()) {
                val point = mEndPoints[i]
                canvas?.drawText(mLogoTexts[i], point.x, point.y, mPaint)
            }
            mGradientMatrix?.setTranslate(mMatrixTranslate.toFloat(), 0f)
            mLinearGradient?.setLocalMatrix(mGradientMatrix)
        }
//        canvas?.drawText(mText, centerX.toFloat(), centerY.toFloat(), mPaint)
    }

    fun setText(text: String) {
        if (text.isNullOrEmpty()) return
        mLogoTexts.clear()
        for (i in text.indices) {
            val c = text[i]
            mLogoTexts.put(i, c.toString())
        }
    }

    fun setTextColor(color: Int) {
        mTextColor = color
        mPaint.color = mTextColor
    }

    private fun start() {
        val anim = ValueAnimator.ofFloat(0f, 1f)
        anim.duration = 800
        anim.interpolator = AccelerateDecelerateInterpolator()
        anim.addUpdateListener {
            if (mRandomPoints.size() <= 0 || mEndPoints.size() <= 0) return@addUpdateListener
            mOffsetAnimProgress = it.animatedValue as Float
            invalidate()
        }
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                if (mGradientAnim != null) {
                    isOffsetAnimEnd = true
                    mPaint.shader = mLinearGradient
                    mGradientAnim?.start()
                }
            }
        })
        anim.start()
    }

    private fun initGradientAnim(width: Int) {
        mGradientAnim = ValueAnimator.ofInt(0, 2 * width)
        mGradientAnim?.duration = 300
        mGradientAnim?.addUpdateListener {
            mMatrixTranslate = it.animatedValue as Int
            invalidate()
        }
        val gradientColor = 0x20000000.toInt()
        mLinearGradient = LinearGradient(
            -width.toFloat(), 0f, 0f, 0f, intArrayOf(mTextColor, gradientColor, mTextColor),
            floatArrayOf(0f, 0.5f, 1f), Shader.TileMode.CLAMP
        )
        mGradientMatrix = Matrix()
    }

    private fun initCoordinate() {
        val centerY = mHeight / 2f + mPaint.textSize / 2 + mLogoOffset
        var totalLength = 0f
        for (i in 0 until mLogoTexts.size()) {
            val str = mLogoTexts.get(i)
            val currentLength = mPaint.measureText(str)
            if (i != mLogoTexts.size() - 1) {
                totalLength += currentLength + mTextPadding
            } else {
                totalLength += currentLength
            }
        }

        if (totalLength > mWidth) {
            throw IllegalStateException("文本太长")
        }
        var startX = (mWidth - totalLength) / 2
        mEndPoints.clear()
        for (i in 0 until mLogoTexts.size()) {
            val str = mLogoTexts[i]
            val currentLength = mPaint.measureText(str)
            mEndPoints.put(i, PointF(startX, centerY))
            startX += currentLength + mTextPadding
        }

        mRandomPoints.clear()

        for (i in 0 until mLogoTexts.size()) {
            mRandomPoints.put(
                i,
                PointF((Math.random() * mWidth).toFloat(), (Math.random() * mHeight).toFloat())
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        initCoordinate()
        initGradientAnim(w)
        start()
    }

}