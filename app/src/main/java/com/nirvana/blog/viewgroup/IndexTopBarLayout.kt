package com.nirvana.blog.viewgroup

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.google.android.material.appbar.AppBarLayout

/**
 * 自定义布局，让其不要响应vp的滑动
 */
class IndexTopBarLayout(context: Context, attrs: AttributeSet) : AppBarLayout(context, attrs) {

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return true
    }

}