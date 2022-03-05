package com.nirvana.blog.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 * 防止滑动冲突的布局
 */
class AvoidDragLayout(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }
}