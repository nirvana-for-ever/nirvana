package com.nirvana.blog.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import com.nirvana.blog.R
import kotlinx.coroutines.*
import kotlin.math.abs

class VerifySeekBar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    /**
     * 背景有关参数
     */
    private val bgPaintNeg = Paint().apply {
        color = ContextCompat.getColor(context, R.color.light_gray)
    }
    private val bgPaintPos = Paint().apply {
        color = ContextCompat.getColor(context, R.color.me_user_login_verify_pos_color)
    }

    /**
     * 滑动提示文字有关参数
     */
    private val textPaint = Paint().apply {
        textAlign = Paint.Align.CENTER
    }
    private val verifyUnpassText = "请按住滑块，拖动到最右边"
    private val verifyPassText = "验证通过"
    private val verifyingText = "验证中"
    private val textRect = Rect()
    private val textColor = ContextCompat.getColor(context, R.color.black_text_color)

    /**
     * 遮罩层有关参数
     */
    private var shadowWidth = 0f
    private var shadowOffset = 0f
    private var shadowThreadStarted = false
    private var shadowJob: Job? = null

    /**
     * 滑块有关参数
     */
    private var draggableX = 0f
    private var draggableY = 0f
    var draggable: View? = null
        set(value) {
            draggableX = value!!.x
            draggableY = value.y
            field = value
        }
    private var draggableDownPos = 0f
    private var stopDragAnimation: ValueAnimator? = null
    var actualDraggable: VerifyDraggable? = null
    private var verifyPass = false
    private var verifying = false
    private val verifyingDrawable = ContextCompat.getDrawable(context, R.drawable.ic_verifying)!!
    private val verifyingDrawableAnim = ValueAnimator.ofFloat(0f, 360f).apply {
        interpolator = LinearInterpolator()
        repeatMode = ValueAnimator.RESTART
    }


    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerifySeekBar)
        textPaint.textSize = typedArray.getDimensionPixelSizeOrThrow(R.styleable.VerifySeekBar_vsb_textSize).toFloat()
        shadowWidth = typedArray.getDimensionPixelSizeOrThrow(R.styleable.VerifySeekBar_vsb_shadowWidth).toFloat()
        typedArray.recycle()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.apply {
            /*
             * 背景
             */
            drawRect(0f, 0f, draggable!!.x, height.toFloat(), bgPaintPos)
            drawRect(draggable!!.x, 0f, width.toFloat(), height.toFloat(), bgPaintNeg)

            /*
             * 文字
             */
            val fontMetrics = textPaint.fontMetrics
            val baseline = abs((fontMetrics.top + fontMetrics.bottom) / 2) + height.toFloat() / 2
            when {
                verifyPass -> {
                    // 通过了行为验证
                    textPaint.color = Color.WHITE
                    textPaint.shader = null
                    drawText(verifyPassText, width.toFloat() / 2, baseline, textPaint)
                }
                verifying -> {
                    // 正在验证行为
                    textPaint.color = Color.WHITE
                    textPaint.shader = null
                    drawText(verifyingText, width.toFloat() / 2, baseline, textPaint)
                    textPaint.getTextBounds(verifyingText, 0, verifyingText.length, textRect)
                    verifyingDrawable.apply {
                        val verifyingLeft = (width.toFloat() + textRect.width()) / 2
                        val verifyingTop = baseline + fontMetrics.top
                        val verifyingBottom = baseline + fontMetrics.bottom
                        val verifyingRight = verifyingBottom - verifyingTop + verifyingLeft
                        setBounds(verifyingLeft.toInt(), verifyingTop.toInt(), verifyingRight.toInt(), verifyingBottom.toInt())
                        save()
                        rotate(
                            verifyingDrawableAnim.animatedValue as Float,
                            (verifyingLeft + verifyingRight) / 2,
                            (verifyingTop + verifyingBottom) / 2,
                        )
                        draw(canvas)
                        restore()
                    }
                }
                else -> {
                    /*
                     * 给文字添加提示动画，让文字颜色有一个动态变化的效果，从文字的左侧平移到右侧，提示用户将滑块从左滑到右
                     */
                    textPaint.getTextBounds(verifyUnpassText, 0, verifyUnpassText.length, textRect)
                    // 计算遮罩层的四条边的位置
                    val leftShadow = (width.toFloat() - textRect.width()) / 2 - shadowWidth
                    val rightShadow = width.toFloat() - leftShadow
                    val topShadow = baseline + fontMetrics.top
                    val bottomShadow = baseline + fontMetrics.bottom

                    val shaderWidth = rightShadow - leftShadow
                    val shadowWidthPercent = shadowWidth / shaderWidth
                    val centerVerticalShadow = (topShadow + bottomShadow) / 2
                    // 设置文字画笔的 shader 用作遮罩层的渐变色
                    textPaint.shader = LinearGradient(
                        leftShadow, centerVerticalShadow, rightShadow, centerVerticalShadow,
                        // 渐变色从左往右，文字颜色由原本的颜色变成白色，再变成原本的颜色
                        intArrayOf(
                            textColor,
                            textColor,
                            Color.WHITE,
                            textColor,
                            textColor
                        ),
                        // 与上面的 int 数组对应，设置各个百分比的颜色，让 shader 自己渐变
                        floatArrayOf(
                            0f,
                            shadowOffset / shaderWidth,
                            (shadowOffset / shaderWidth) + (shadowWidthPercent * 0.5f),
                            (shadowOffset / shaderWidth) + (shadowWidthPercent * 1f),
                            1f
                        ),
                        Shader.TileMode.CLAMP
                    )
                    drawText(verifyUnpassText, width.toFloat() / 2, baseline, textPaint)
                }
            }
        }

        if (!shadowThreadStarted) {
            shadowThreadStarted = true
            startShadow()
        }
    }

    /**
     * 开一个线程来完成动态变化遮罩层的位置
     */
    private fun startShadow() {
        val width = textRect.width() + shadowWidth
        shadowJob = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                if (shadowOffset + 3 <= width) {
                    shadowOffset += 3
                } else {
                    shadowOffset = 0f
                    delay(2000)
                }
                postInvalidate()
                delay(4)
            }
        }
    }

    /**
     * 画可滑动的滑块事件监听
     * 必须要设置 draggable 属性，否则报错
     * 通过动态设置 draggable 的 x 值来改变滑块的位置，滑块本身不设置触摸事件
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!verifyPass && !verifying) {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    // 滑动一开始就记录下滑动的初始 x
                    draggableDownPos = event.x - (stopDragAnimation?.animatedValue ?: 0f) as Float
                    // 停止往回收的动画
                    stopDragAnimation?.cancel()
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    stopDrag()
                    return true
                }
                MotionEvent.ACTION_MOVE -> {
                    // 开始滑动，event.x 是手指距离view的start的距离而不是划过的距离
                    // 所以用其 - 初始x 就是划过的距离
                    val newX = event.x - draggableDownPos
                    // 保证不要滑到超出view的范围
                    val dragEnd = newX > width + marginStart + marginEnd -
                            draggable!!.width + draggable!!.marginEnd + draggable!!.marginStart
                    if (newX >= draggableX && !dragEnd && !verifying && !verifyPass) {
                        // 改变 draggable 的 x 值来改变滑块的位置
                        draggable!!.x = event.x - draggableDownPos
                        invalidate()
                    }
                    if (dragEnd) verify()
                    return true
                }
                // 多指触控事件，视为滑动终止
                MotionEvent.ACTION_POINTER_DOWN -> {
                    stopDrag()
                    return false
                }
            }
        }
        return super.onTouchEvent(event)
    }

    /**
     * 验证是否通过，具体有什么验证是否人机的逻辑暂时不清楚，
     * 统一认为：滑到底就验证通过
     */
    private fun verify() {
        CoroutineScope(Dispatchers.IO).launch {
            // 假装正在验证
            verifying = true
            withContext(Dispatchers.Main) {
                verifyingDrawableAnim.start()
            }
            verifyingDrawableAnim.addUpdateListener {
                postInvalidate()
            }
            postInvalidate()

            // 假装正在验证
            delay(500)

            // 验证成功
            verifying = false
            withContext(Dispatchers.Main) {
                verifyingDrawableAnim.cancel()
            }
            verifyPass = true
            draggable!!.x = width.toFloat() + marginStart + marginEnd -
                    draggable!!.width + draggable!!.marginEnd + draggable!!.marginStart
            actualDraggable?.verifyPass()
            postInvalidate()
            shadowJob?.cancel()
        }
    }

    /**
     * 滑动终止，将滑块放回原位
     */
    private fun stopDrag() {
        stopDragAnimation =
            ValueAnimator.ofFloat(draggable!!.x, draggableX).apply {
                duration = 300L
                interpolator = LinearInterpolator()
            }
        stopDragAnimation!!.addUpdateListener {
            draggable!!.x = it.animatedValue as Float
            invalidate()
        }
        stopDragAnimation!!.start()
    }

    fun isVerifyPass() = verifyPass

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        if (shadowJob?.isActive == true) {
            shadowJob?.cancel()
        }
        if (stopDragAnimation?.isRunning == true) {
            stopDragAnimation?.cancel()
        }
        if (verifyingDrawableAnim?.isRunning == true) {
            verifyingDrawableAnim.cancel()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return Bundle().apply {
            putParcelable("super", superState)
            putBoolean("verifyPass", verifyPass)
            if (verifyPass) {
                putFloat("draggableX", draggable!!.x)
            }
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as Bundle).apply {
            super.onRestoreInstanceState(getParcelable("super"))
            verifyPass = getBoolean("verifyPass")
            if (verifyPass) {
                draggable!!.x = getFloat("draggableX")
                actualDraggable?.verifyPass()
            }
        }
    }

}

open class VerifyDraggable(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val bgPaint = Paint().apply {
        color = Color.WHITE
    }

    private var iconDrawable: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_verify_draggable)!!

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
            iconDrawable.apply {
                setBounds(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom)
                draw(canvas)
            }
        }
    }

    fun verifyPass() {
        iconDrawable = ContextCompat.getDrawable(context, R.drawable.ic_verify_pass)!!
        postInvalidate()
    }

}
