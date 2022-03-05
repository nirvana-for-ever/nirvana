package com.nirvana.blog.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.nirvana.blog.R
import com.nirvana.blog.utils.DensityUtils.dip2px

/**
 * 轮播图下方滚动的点
 * 通过 [setSelectedDotX] 方法设置动画，配合 vp 的 onPageScrolled 的参数即可实现动画
 */
class CarouselDots(context: Context, attrs: AttributeSet) : View(context, attrs) {

    // 点半径
    private val dotRadius: Int
    // 点个数，需要在代码中手动设置
    var dotCount: Int = 1
    private val dotSelectedColor: Int
    private val dotUnSelectedColor: Int

    // 画被选中的点的画笔
    private val selectedPaint = Paint()
    // 画其他点的画笔
    private val unSelectedPaint = Paint()

    // 第一个点的 x 坐标，通常是点的半径
    private var firstDotX = 0f
    // 每个点相距的 x 值
    private var eachX = 0
    // 被选中的点的下标，是动态变化的，也就是 vp 中回调方法 onPageScrolled 的 position 参数
    // 受到 setSelectedDotX 的影响，随着 vp 的页变化而变化
    private var selectedDotIndex = 0
    // vp 中回调方法 onPageScrolled 的 positionOffset 参数
    private var selectedDotPercent = 0f

    init {
        // 获取属性值
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CarouselDots)
        dotRadius = typedArray.getDimensionPixelSize(R.styleable.CarouselDots_dotRadius, dip2px(5f))
        dotSelectedColor = typedArray.getColor(R.styleable.CarouselDots_dotSelectedColor, Color.WHITE)
        dotUnSelectedColor = typedArray.getColor(R.styleable.CarouselDots_dotUnSelectedColor, Color.BLACK)
        typedArray.recycle()

        selectedPaint.color = dotSelectedColor
        unSelectedPaint.color = dotUnSelectedColor
        firstDotX = dotRadius.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            /*
             * 画所有的点
             */
            if (dotCount == 1) {
                drawCircle(width.toFloat() / 2, height.toFloat() / 2, dotRadius.toFloat(), selectedPaint)
                return
            }
            eachX = ((width - dotRadius * 2) / (dotCount - 1)).takeIf { it > 0 } ?: throw RuntimeException()
            var curX = firstDotX
            repeat(dotCount) {
                drawCircle(curX, height.toFloat() / 2, dotRadius.toFloat(), unSelectedPaint)
                curX += eachX
            }
            /*
             * 画正在变化的点
             */
            drawCircle(
                firstDotX + eachX * (selectedDotIndex + selectedDotPercent),
                height.toFloat() / 2,
                dotRadius.toFloat(),
                selectedPaint
            )
        }
    }

    /**
     * 设置被选中的点的位置，可用做动画
     * @param position 从第 1 页移动到第 2 页的话，position 就是 1
     *                  从第 2 页移动到第 1 页的话，position 还是 1
     *                  总而言之，就是移动的两个相关页中下标较小的那个下标
     * @param percent 移动了百分之几，根据这个来设置被选中的点的位置
     */
    fun setSelectedDotX(position: Int, percent: Float) {
        if (dotCount > 1) {
            /*
             * 如果是从最后一个和第一个之间的滑动，需要特殊处理，在滑动超过一半时，就将被选中的点直接放在第一个点的位置
             * 如果滑动小于一半，就放在最后一个点的位置
             */
            if (position == dotCount - 1) {
                selectedDotIndex = if (percent > 0.5) 0 else dotCount - 1
                selectedDotPercent = 0f
            } else {
                selectedDotIndex = position
                selectedDotPercent = percent
            }
            invalidate()
        }
    }

}