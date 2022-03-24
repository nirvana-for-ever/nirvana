package com.nirvana.blog.entity.ui

data class UiResult<T>(val success: Boolean, val msg: String, val data: T? = null)
