package com.nirvana.blog.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.content.res.getDrawableOrThrow
import com.gyf.immersionbar.ImmersionBar
import com.nirvana.blog.R

/**
 * “我的”界面的横幅
 */
class MeBanner(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val paint = Paint()

    // 横幅的渐变色
    private var startColor = 0
    private var endColor = 0
    // 起始和结束的两个图片以及尺寸，只能指定正方形的图片
    private var startPic: Drawable
    private var endPic: Drawable
    private var startPicSize = 0
    private var endPicSize = 0
    // 起始和结束的两个图片的水平边距
    private var picPaddingHor = 0
    // actionbar 的高度，让图片与 actionbar 的图标的位置一致
    private var actionBarHeight = 0

    init {
        // 获取属性值
        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.MeBanner)
        startColor = typedArray.getColor(R.styleable.MeBanner_startColor, 0)
        endColor = typedArray.getColor(R.styleable.MeBanner_endColor, 0)
        startPic = typedArray.getDrawableOrThrow(R.styleable.MeBanner_startPic)
        endPic = typedArray.getDrawableOrThrow(R.styleable.MeBanner_endPic)
        startPicSize = typedArray.getDimensionPixelSizeOrThrow(R.styleable.MeBanner_startPicSize)
        endPicSize = typedArray.getDimensionPixelSizeOrThrow(R.styleable.MeBanner_endPicSize)
        picPaddingHor = typedArray.getDimensionPixelSize(R.styleable.MeBanner_picPaddingHor, 0)
        actionBarHeight = typedArray.getDimensionPixelSize(R.styleable.MeBanner_actionBarHeight, 0)
        typedArray.recycle()
    }

    /**
     * 很简单，就是用一个 shader 画一个椭圆
     */
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val bannerHeight = if (layoutParams.height > 0) {
            layoutParams.height.toFloat()
        } else {
            throw IllegalArgumentException("MeBanner 必须指定确定的高度")
        }

        paint.apply {
            shader = LinearGradient(
                0f, height.toFloat() / 2,
                width.toFloat(), height.toFloat() / 2,
                startColor, endColor, Shader.TileMode.CLAMP
            )
        }
        canvas?.apply {
            drawOval(-bannerHeight, -bannerHeight, width.toFloat() + bannerHeight, bannerHeight, paint)

            /*
             * 画左右两个图标，用的是 Drawable 的 draw 方法，传入 Canvas 对象
             * 注意，需要设置 Drawable 的 Bounds 信息才能绘画出来，也就是指定四条边
             * 图片会在指定的 Bounds 中进行绘制
             */
            val statusBarHeight = ImmersionBar.getStatusBarHeight(context)

            val startPicTop = statusBarHeight + actionBarHeight / 2 - startPicSize / 2
            val endPicTop = statusBarHeight + actionBarHeight / 2 - endPicSize / 2
            startPic.setBounds(
                picPaddingHor,
                startPicTop,
                startPicSize + picPaddingHor,
                startPicTop + startPicSize
            )
            startPic.draw(canvas)

            endPic.setBounds(
                width - picPaddingHor - endPicSize,
                endPicTop,
                width - picPaddingHor,
                endPicTop + endPicSize
            )
            endPic.draw(canvas)
        }
    }

}