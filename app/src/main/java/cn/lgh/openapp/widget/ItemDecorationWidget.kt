package cn.lgh.openapp.widget

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class ItemDecorationWidget(
    var mSpaceHorizontal: Int,
    var mSpaceVertical: Int,
    var spanCount: Int? = 1,
    var includeEdge: Boolean = false, //grid 是否包含左右边距
    var includeHeadFooter: Boolean = true //头尾是否有间距，默认有
) : RecyclerView.ItemDecoration() {

    var mPaint: Paint? = null
    var mColor: Int = 0

    init {
        mPaint = Paint()
        mPaint?.style = Paint.Style.FILL
        mPaint?.isAntiAlias = true
    }

    fun setColor(color: Int) {
        mColor = color
        mPaint?.color = color
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (mColor == 0) return
        var layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
        } else if (layoutManager is LinearLayoutManager) {
            drawLinearItemSpace(c, parent, layoutManager)
        } else if (layoutManager is StaggeredGridLayoutManager) {
        }
    }

    private fun drawLinearItemSpace(
        c: Canvas,
        parent: RecyclerView,
        layoutManager: LinearLayoutManager
    ) {
        if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
            for (viewPosition in 0..layoutManager.itemCount) {
                val childView: View = parent.getChildAt(viewPosition) ?: continue

                val params = childView.layoutParams as RecyclerView.LayoutParams

                var left = 0
                var top = 0
                var right = 0
                var bottom = 0
                left = childView.left - params.leftMargin
                right = childView.right + params.rightMargin
                top = childView.bottom + params.bottomMargin
                bottom = top + mSpaceVertical
                mPaint?.let {
                    c.drawRect(
                        left.toFloat(),
                        top.toFloat(),
                        right.toFloat(),
                        bottom.toFloat(),
                        it
                    )
                }

                if (viewPosition == 0) {
                    top = childView.top - params.topMargin - mSpaceVertical
                    bottom = top + mSpaceVertical
                    mPaint?.let {
                        c.drawRect(
                            left.toFloat(),
                            top.toFloat(),
                            right.toFloat(),
                            bottom.toFloat(),
                            it
                        )
                    }
                }
            }
        } else {

            for (viewPosition in 0..layoutManager.itemCount) {
                val childView: View = parent.getChildAt(viewPosition) ?: continue

                val params = childView.layoutParams as RecyclerView.LayoutParams

                var left = 0
                var top = 0
                var right = 0
                var bottom = 0

                left = childView.right + params.rightMargin
                top = childView.top - params.topMargin
                right = left + mSpaceHorizontal
                bottom = childView.bottom + params.bottomMargin
                mPaint?.let {
                    c.drawRect(
                        left.toFloat(),
                        top.toFloat(),
                        right.toFloat(),
                        bottom.toFloat(),
                        it
                    )
                }

                if (viewPosition == 0) {
                    left = childView.left - params.leftMargin - mSpaceHorizontal
                    top = childView.top - params.topMargin
                    right = left + mSpaceHorizontal
                    bottom = childView.bottom + params.bottomMargin
                    mPaint?.let {
                        c.drawRect(
                            left.toFloat(),
                            top.toFloat(),
                            right.toFloat(),
                            bottom.toFloat(),
                            it
                        )
                    }
                }
            }

        }
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            layoutGridItemSpace(outRect, view, parent, layoutManager)
        } else if (layoutManager is LinearLayoutManager) {
            layoutLinearItemSpace(outRect, view, parent, layoutManager)
        } else if (layoutManager is StaggeredGridLayoutManager) {
            layoutStaggeredItemSpace(outRect, view, parent, layoutManager)
        }

    }

    //思想：如果是垂直布局，
    private fun layoutLinearItemSpace(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        layoutManager: LinearLayoutManager
    ) {
        var viewPosition = parent.getChildAdapterPosition(view)

        if (layoutManager.orientation == LinearLayoutManager.VERTICAL) {
            outRect.left = mSpaceHorizontal
            outRect.right = mSpaceHorizontal
            if (viewPosition == 0) {
                outRect.top = if (includeHeadFooter) mSpaceVertical else 0
                outRect.bottom = mSpaceVertical / 2
            } else if (viewPosition == layoutManager.itemCount - 1) {
                outRect.top = mSpaceVertical / 2
                outRect.bottom = if (includeHeadFooter) mSpaceVertical else 0
            } else {
                outRect.top = mSpaceVertical / 2
                outRect.bottom = mSpaceVertical / 2
            }
        } else {
            outRect.top = mSpaceVertical
            outRect.bottom = mSpaceVertical

            if (viewPosition == 0) {
                outRect.left = mSpaceHorizontal
                outRect.right = mSpaceHorizontal / 2
            } else if (viewPosition == layoutManager.itemCount - 1) {
                outRect.left = mSpaceHorizontal / 2
                outRect.right = mSpaceHorizontal
            } else {
                outRect.left = mSpaceHorizontal / 2
                outRect.right = mSpaceHorizontal / 2
            }
        }
    }

    private fun layoutGridItemSpace(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        layoutManager: GridLayoutManager
    ) {
        val position = parent.getChildAdapterPosition(view)

        val lp = view.layoutParams as GridLayoutManager.LayoutParams
        val spanIndex = lp.spanIndex
        val spanSize = lp.spanSize

        val spanCount = this.spanCount!! / spanSize

        val column = spanIndex / spanSize //item column

//        println("lgh  position=$position spanIndex=$spanIndex  spanSize=$spanSize spanCount=$spanCount column=$column")

        if (includeEdge) {
            outRect.left = mSpaceHorizontal - column * mSpaceHorizontal / spanCount
            outRect.right = (column + 1) * mSpaceHorizontal / spanCount

            if (position < spanCount) {
                outRect.top = mSpaceVertical
            }
            outRect.bottom = mSpaceVertical
        } else {
            outRect.left =
                column * mSpaceHorizontal / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right =
                mSpaceHorizontal - (column + 1) * mSpaceHorizontal / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = mSpaceVertical
            }
        }
    }

    private fun layoutStaggeredItemSpace(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        layoutManager: StaggeredGridLayoutManager
    ) {

    }

}