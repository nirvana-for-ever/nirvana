package com.nirvana.blog.entity.network

data class RespResult<T>(val success: Boolean, val code: Int, val message: String, val data: T? = null)

data class PagingResult<T>(val list: List<T>, val hasNext: Boolean)
