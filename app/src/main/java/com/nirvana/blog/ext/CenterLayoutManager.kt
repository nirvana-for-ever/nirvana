package com.nirvana.blog.ext

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller

/**
 * 如果要实现 RecyclerView 调用 smoothScrollToPosition 方法时，让目标 position 的 item 居中，
 * 就使用这个 LayoutManager 来替代 LinearLayoutManager
 */
class CenterLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    /**
     * 固定写法，参照 LinearLayoutManager 的写法，只不过将 LinearSmoothScroller 改为下面重写的 CenterSmoothScroller
     */
    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val smoothScroller: SmoothScroller = CenterSmoothScroller(recyclerView.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    /**
     * 重点方法，用该方法计算出滚动距离，结果的正负代表方向
     *
     * boxStart 是当前 recyclerview 可见部分的开始坐标
     * boxEnd 是 recyclerview 可见部分的结束坐标，我们需要移动到的中点坐标为 boxStart + (boxEnd - boxStart) / 2,
     * viewStart 是将要移动的目标 item 的开始坐标，可能比 boxEnd 大（往后滑动），也可能比 boxStart 小（往前滑动）
     * viewEnd 是将要移动的目标 item 的结束坐标
     * 而该 view 的中心点坐标是 viewStart + (viewEnd - viewStart) / 2
     * 上述两位置相减有正负，所得结果为该 item 需要偏移滚动的距离。
     */
    private class CenterSmoothScroller(context: Context?) :
        LinearSmoothScroller(context) {
        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int
        ): Int {
            return boxStart + (boxEnd - boxStart) / 2 - (viewStart + (viewEnd - viewStart) / 2)
        }

        /**
         * 设置 Smooth 滚动的滚动速度，一般用 x / displayMetrics.densityDpi 来指定速度
         * 返回值代表每个像素应该花费的时间（以毫秒为单位），默认是 25f / displayMetrics.densityDpi
         * 太快了，这里设置成 100f
         */
        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return 100f / displayMetrics.densityDpi
        }
    }
}
