package cn.lgh.openapp.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import cn.lgh.openapp.R

/**
 * @author lgh
 * @date 2020/10/10
 * 流布局
 */
class FlowLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    var horizontalSpacing: Float
    var verticalSpacing: Float

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout)
        horizontalSpacing = a.getDimension(R.styleable.FlowLayout_horizontalSpacing, 0f)
        verticalSpacing = a.getDimension(R.styleable.FlowLayout_verticalSpacing, 0f)
        a.recycle()
    }

    private var adapter: BaseFlowAdapter? = null

    fun setAdapter(adapter: BaseFlowAdapter?) {
        this.adapter = adapter
        adapter?.registerObserver {
            removeAllViews()
            addAll(this.adapter)
        }
        addAll(adapter)
    }

    private fun addAll(adapter: BaseFlowAdapter?) {
        adapter?.let {
            for (i in 0 until it.getCount()) {
                addView(
                    it.getView(i),
                    MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                        if (i == 0) {
                            marginStart = 0
                            marginEnd = (horizontalSpacing / 2f).toInt()
                        } else if (i == it.getCount() - 1) {
                            marginStart = (horizontalSpacing / 2f).toInt()
                            marginEnd = 0
                        } else {
                            marginStart = (horizontalSpacing / 2f).toInt()
                            marginEnd = (horizontalSpacing / 2f).toInt()
                        }
                        topMargin = (verticalSpacing / 2f).toInt()
                        bottomMargin = (verticalSpacing / 2f).toInt()
                    }
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //首先测量所有子view
        //测量子view才能知道子view的宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        var measuredWidth = 0
        var measuredHeight = 0

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        //由于计算子view所占宽度，这里传值需要自身减去paddingRight宽度
        val compute = compute(widthSize - paddingRight)

        //EXACTLY ： 对应给定大小或者match_parent情况
        if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = widthSize
        } else if (widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = compute["allChildWidth"]!!
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            measuredWidth = compute["allChildWidth"]!!
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = heightSize
        } else if (heightMode == MeasureSpec.AT_MOST) {
            measuredHeight = compute["allChildHeight"]!!
        } else if (heightMode == MeasureSpec.UNSPECIFIED) {
            //如果要嵌套在recyclerView上，这个测量模式必须实现
            //具体源码可以看RecyclerView.getChildMeasureSpec()
            measuredHeight = compute["allChildHeight"]!!
        }
        setMeasuredDimension(measuredWidth, measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val rect = child.tag as Rect
            child.layout(rect.left, rect.top, rect.right, rect.bottom)
        }
    }


    /**
     * 测量子元素总得高度和宽度
     */
    private fun compute(flowWidth: Int): MutableMap<String, Int> {
        //是否单行
        var aRow = true
        var marginParams: MarginLayoutParams
        var rowsWidth = paddingLeft //当前行已占宽度(注意需要加上paddingLeft)
        var columnHeight = paddingTop //当前行顶部已占高度(注意需要加上paddingTop)
        var rowsMaxHeight = 0 //当前行所有子元素的最大高度（用于换行累加高度）

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            //获取子view的测量宽高
            val measureWidth = child.measuredWidth
            val measureHeight = child.measuredHeight
            //子view的margin
            if (child.layoutParams !is MarginLayoutParams) {
                child.layoutParams = MarginLayoutParams(child.layoutParams)
            }
            marginParams = child.layoutParams as MarginLayoutParams
            //计算子view所占的宽高
            val childWidth = marginParams.leftMargin + marginParams.rightMargin + measureWidth
            val childHeight = marginParams.topMargin + marginParams.bottomMargin + measureHeight

            rowsMaxHeight = rowsMaxHeight.coerceAtLeast(childHeight)
            //判断是否换行: 该行已占大小+该元素大小>父容器宽度 则换行
            if (rowsWidth + childWidth > flowWidth) {
                //重置行宽度
                rowsWidth = paddingLeft + paddingRight
                //换行后累加当前子view高度，就是在另一行显示
                columnHeight += rowsMaxHeight
                //重置高度
                rowsMaxHeight = childHeight
                aRow = false
            }
            //累加当前元素的宽度
            rowsWidth += childWidth
            //判断时占的宽度加上margin计算，设置定点时不包括margin位置，不然margin不起作用
            child.tag = Rect(
                rowsWidth - childWidth + marginParams.leftMargin,
                columnHeight + marginParams.topMargin,
                rowsWidth - marginParams.rightMargin,
                columnHeight + childHeight - marginParams.bottomMargin
            )
        }
        val flowMap = mutableMapOf<String, Int>()
        if (aRow) {
            //单行
            flowMap["allChildWidth"] = rowsWidth
        } else {
            //多行
            flowMap["allChildWidth"] = flowWidth
        }
        //FlowLayout 测量高度=当前行顶部已占高度+当前行内子元素最大高度+FlowLayout的padding
        flowMap["allChildHeight"] = columnHeight + rowsMaxHeight + paddingBottom
        return flowMap
    }


    abstract class BaseFlowAdapter {

        private val observes = mutableListOf<(() -> Unit)?>()

        fun registerObserver(observer: (() -> Unit)?) {
            observes.add(observer)
        }

        abstract fun getCount(): Int
        abstract fun getView(position: Int): View

        fun notifyDataSetChanged() {
            observes.forEach {
                it?.invoke()
            }
        }
    }
}