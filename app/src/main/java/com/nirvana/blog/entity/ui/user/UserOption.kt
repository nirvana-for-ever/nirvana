package com.nirvana.blog.entity.ui.user

import android.view.View

/**
 * 我的页面，下方的用户选项菜单项
 */
data class UserOption(val title: String, val subtitle: String = "", val onClick: ((View) -> Unit)? = null)
