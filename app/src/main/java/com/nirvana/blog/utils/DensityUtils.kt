package com.nirvana.blog.utils

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment

object DensityUtils {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density;
        return (dpValue * scale + 0.5f).toInt()
    }
    fun Activity.dip2px(dpValue: Float): Int = dip2px(this, dpValue)
    fun Fragment.dip2px(dpValue: Float): Int = dip2px(requireContext(), dpValue)
    fun View.dip2px(dpValue: Float): Int = dip2px(context, dpValue)

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density;
        return (pxValue / scale + 0.5f).toInt()
    }
    fun Activity.px2dip(dpValue: Float): Int = px2dip(this, dpValue)
    fun Fragment.px2dip(dpValue: Float): Int = px2dip(requireContext(), dpValue)
    fun View.px2dip(dpValue: Float): Int = px2dip(context, dpValue)


    /******************************************************************************/

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }
    fun Activity.px2sp(pxValue: Float): Int = px2sp(this, pxValue)
    fun Fragment.px2sp(pxValue: Float): Int = px2sp(requireContext(), pxValue)
    fun View.px2sp(pxValue: Float): Int = px2sp(context, pxValue)

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
    fun Activity.sp2px(spValue: Float): Int = sp2px(this, spValue)
    fun Fragment.sp2px(spValue: Float): Int = sp2px(requireContext(), spValue)
    fun View.sp2px(spValue: Float): Int = sp2px(context, spValue)
}