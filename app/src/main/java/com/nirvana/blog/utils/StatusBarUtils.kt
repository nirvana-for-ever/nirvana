package com.nirvana.blog.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import com.nirvana.blog.R

object StatusBarUtils {
    /**
     * 获取状态栏高度
     * 可以用 ImmersionBar.getStatusBarHeight(this) 替代
     */
    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }
        return statusBarHeight
    }

    /**
     * 设置基本的状态栏样式
     */
    fun Activity.setBaseStatusBar(other: (ImmersionBar.() -> Unit)? = null) {
        immersionBar {
            // 透明状态栏
            transparentStatusBar()
            // 暗色状态栏字体
            statusBarDarkFont(true)
            // 底部导航栏颜色，也就是菜单，主页，返回栏的颜色
            navigationBarColor(R.color.white)
            other?.let { it() }
        }
    }
    fun Fragment.setBaseStatusBar(other: (ImmersionBar.() -> Unit)? = null) {
        immersionBar {
            // 透明状态栏
            transparentStatusBar()
            // 暗色状态栏字体
            statusBarDarkFont(true)
            // 底部导航栏颜色，也就是菜单，主页，返回栏的颜色
            navigationBarColor(R.color.white)
            other?.let { it() }
        }
    }
}