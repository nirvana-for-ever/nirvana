package com.nirvana.blog.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.nirvana.blog.utils.StatusBarUtils

class StatusBarPlaceHolder(context: Context, attrs: AttributeSet) : View(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            getDefaultSize(suggestedMinimumWidth, widthMeasureSpec),
            resolveSize(StatusBarUtils.getStatusBarHeight(context), heightMeasureSpec)
        )
    }
}