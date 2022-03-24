package com.nirvana.blog.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.getDimensionOrThrow
import androidx.core.view.updatePadding
import com.nirvana.blog.R

/**
 * 气泡文字
 */
class BubbleTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs) {

    companion object {
        /**
         * 箭头位置的常量
         */
        const val POSITION_LEFT = 0
        const val POSITION_TOP = 1
        const val POSITION_RIGHT = 2
        const val POSITION_BOTTOM = 3
    }

    private val bgPaint: Paint

    private val arrowPaint: Paint

    private val bgRectF = RectF()

    private val arrowPath = Path()

    /*********************************attrs*************************************/
    /**
     * 气泡箭头位置
     */
    private val position: Int

    /**
     * 气泡箭头大小
     */
    private val arrowSize: Float

    /**
     * 气泡偏移量，以逆时针初始位置为0，
     * 比如箭头放在底部，offset为0时，就是左下角的箭头，offset增加就往右偏移
     * 比如箭头放在左侧，offset为0时，就是左上角的箭头，offset增加就往下偏移
     */
    private val arrowOffset: Float

    /**
     * 背景颜色
     */
    private val bgColor: Int

    /**
     * 圆角半径
     */
    private val borderRadius: Float

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BubbleTextView)
        position = typedArray.getInt(R.styleable.BubbleTextView_bbtv_position, POSITION_LEFT)
        arrowSize = typedArray.getDimensionOrThrow(R.styleable.BubbleTextView_bbtv_arrowSize)
        bgColor = typedArray.getColor(R.styleable.BubbleTextView_bbtv_bgColor, Color.TRANSPARENT)
        borderRadius = typedArray.getDimension(R.styleable.BubbleTextView_bbtv_borderRadius, 0f)
        // 箭头需要避免画在圆角上，很尼玛丑
        arrowOffset = typedArray.getDimension(R.styleable.BubbleTextView_bbtv_arrowOffset, 0 + borderRadius)
        typedArray.recycle()

        when (position) {
            POSITION_LEFT -> updatePadding(left = paddingLeft + arrowSize.toInt())
            POSITION_TOP -> updatePadding(top = paddingTop + arrowSize.toInt())
            POSITION_RIGHT -> updatePadding(right = paddingRight + arrowSize.toInt())
            POSITION_BOTTOM -> updatePadding(bottom = paddingBottom + arrowSize.toInt())
        }

        bgPaint = Paint().apply {
            color = bgColor
        }

        arrowPaint = Paint().apply {
            color = bgColor
            // 箭头转角稍微圆润一点
            pathEffect = CornerPathEffect(arrowSize / 2)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            bgRectF.set(0f, 0f, width.toFloat(), height.toFloat())
            when (position) {
                POSITION_LEFT -> {
                    bgRectF.left += arrowSize
                    arrowPath.apply {
                        moveTo(arrowSize, arrowOffset)
                        rLineTo(-arrowSize, arrowSize / 2)
                        rLineTo(arrowSize, arrowSize / 2)
                    }
                }
                POSITION_TOP -> {
                    bgRectF.top += arrowSize
                    arrowPath.apply {
                        moveTo(width - arrowOffset, arrowSize)
                        rLineTo(-arrowSize / 2, -arrowSize)
                        rLineTo(-arrowSize / 2, arrowSize)
                    }
                }
                POSITION_RIGHT -> {
                    bgRectF.right -= arrowSize
                    arrowPath.apply {
                        moveTo(width - arrowSize, height - arrowOffset)
                        rLineTo(arrowSize, -arrowSize / 2)
                        rLineTo(-arrowSize, -arrowSize / 2)
                    }
                }
                POSITION_BOTTOM -> {
                    bgRectF.bottom -= arrowSize
                    arrowPath.apply {
                        moveTo(arrowOffset, height - arrowSize)
                        rLineTo(arrowSize / 2, arrowSize)
                        rLineTo(arrowSize / 2, -arrowSize)
                    }
                }
            }
            drawRoundRect(bgRectF, borderRadius, borderRadius, bgPaint)
            drawPath(arrowPath, arrowPaint)
        }
        super.onDraw(canvas)
    }

}