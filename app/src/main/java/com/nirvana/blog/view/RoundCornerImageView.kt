package com.nirvana.blog.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.nirvana.blog.utils.DensityUtils.dip2px

/**
 * 带圆角设置的 ImageView ，实现了 AppCompatImageView
 *
 * 为啥不直接实现 ImageView？
 * AppCompatTextView,AppCompatImageView等等AppCompatXxView是在support v7中引入的，
 * 可以使用高版本的才有的一些特性，比如tint着色的部分功能，是在L开始才有的，使用AppCompatView之后可以向低版本兼容。
 * 当我们的Activity继承自AppCompatActivity时，
 * 内部自动会将我们在布局中正常View对象转换为对应的AppCompatView对象来使用
 *
 * 通过 Path 来实现
 */
class RoundCornerImageView(context: Context, attr: AttributeSet) :
    AppCompatImageView(context, attr) {

    private val radiusArray = floatArrayOf(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f)

    fun setRadius(
        radius: Float = 0f,
        dp: Boolean = false
    ) {
        if (dp) {
            val r = dip2px(radius).toFloat()
            radiusArray[0] = r
            radiusArray[1] = r
            radiusArray[2] = r
            radiusArray[3] = r
            radiusArray[4] = r
            radiusArray[5] = r
            radiusArray[6] = r
            radiusArray[7] = r
        } else {
            radiusArray[0] = radius
            radiusArray[1] = radius
            radiusArray[2] = radius
            radiusArray[3] = radius
            radiusArray[4] = radius
            radiusArray[5] = radius
            radiusArray[6] = radius
            radiusArray[7] = radius
        }
        invalidate()
    }

    /**
     * 设置四个角的圆角半径
     */
    fun setRadius(
        leftTop: Float = 0f,
        rightTop: Float = 0f,
        rightBottom: Float = 0f,
        leftBottom: Float = 0f,
        dp: Boolean = false
    ) {
        if (dp) {
            val ltDp = dip2px(leftTop).toFloat()
            val rtDp = dip2px(rightTop).toFloat()
            val rbDp = dip2px(rightBottom).toFloat()
            val lbDp = dip2px(leftBottom).toFloat()
            radiusArray[0] = ltDp
            radiusArray[1] = ltDp
            radiusArray[2] = rtDp
            radiusArray[3] = rtDp
            radiusArray[4] = rbDp
            radiusArray[5] = rbDp
            radiusArray[6] = lbDp
            radiusArray[7] = lbDp
        } else {
            radiusArray[0] = leftTop
            radiusArray[1] = leftTop
            radiusArray[2] = rightTop
            radiusArray[3] = rightTop
            radiusArray[4] = rightBottom
            radiusArray[5] = rightBottom
            radiusArray[6] = leftBottom
            radiusArray[7] = leftBottom
        }
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        val path = Path().apply {
            addRoundRect(
                RectF(0f, 0f, width.toFloat(), height.toFloat()),
                radiusArray,
                Path.Direction.CW
            )
        }
        canvas.clipPath(path)
        super.onDraw(canvas)
    }
}