package com.nirvana.blog.utils

import androidx.viewbinding.ViewBinding
import com.nirvana.blog.base.BaseActivity

var rootActivity: BaseActivity<out ViewBinding>? = null

var isLogin = false

var messageLikeClientTime: Long = 0L

var messageCommentClientTime: Long = 0L