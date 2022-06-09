package com.nirvana.blog.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import java.lang.Exception
import java.lang.reflect.Field

/**
 * 默认的 vp 滑动太灵敏了，设置成一般灵敏度
 */
fun ViewPager2.setNormalSensitivity() {
    try {
        val recyclerViewField: Field = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView
        val touchSlopField: Field = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop * 6) // 越大越不灵敏
    } catch (ignore: Exception) {
    }
}