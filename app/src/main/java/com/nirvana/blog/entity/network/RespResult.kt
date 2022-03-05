package com.nirvana.blog.entity.network

data class RespResult<T>(val success: Boolean, val code: Int, val message: String, val data: T)

data class PagingResult<T>(val list: List<T>, val hasNext: Boolean)
