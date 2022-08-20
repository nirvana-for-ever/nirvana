package com.nirvana.blog

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ApplicationInfo
import dagger.hilt.android.HiltAndroidApp

/**
 * 记得在manifest的application标签中添加该类的name
 */
@HiltAndroidApp
class BlogApplication : Application(){

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: ContextWrapper
    }

    private var mActivityList: MutableList<Activity>? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        init()
    }

    fun init() {
        mActivityList = ArrayList()
    }

    fun isApkInDebug(): Boolean {
        return try {
            val info = this.applicationInfo
            info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
        } catch (e: Exception) {
            false
        }
    }

    fun addActivity(activity: Activity) {
        mActivityList!!.add(activity)
    }

    fun getLastActivity(): Activity? {
        return if (mActivityList!!.size > 0) {
            mActivityList!![mActivityList!!.size - 1]
        } else null
    }

    fun removeActivity(activity: Activity) {
        if (mActivityList!!.contains(activity)) {
            mActivityList!!.remove(activity)
        }
    }

    /**
     * 关闭程序所有的Activity
     */
    fun clearActivityList() {
        for (act in mActivityList!!) {
            act.finish()
        }
        mActivityList!!.clear()
    }


}