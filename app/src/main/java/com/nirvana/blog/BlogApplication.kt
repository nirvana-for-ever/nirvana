package com.nirvana.blog

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 记得在manifest的application标签中添加该类的name
 */
@HiltAndroidApp
class BlogApplication : Application()