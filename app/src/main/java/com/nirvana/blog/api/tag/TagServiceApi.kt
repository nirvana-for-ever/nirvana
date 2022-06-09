package com.nirvana.blog.api.tag

import com.nirvana.blog.entity.ui.tag.Tag
import com.nirvana.blog.entity.network.RespResult
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface TagServiceApi {

    /**
     * 查询用户的所有标签
     */
    @GET("main/tag/user")
    suspend fun getTags(): RespResult<MutableList<Tag>>

    /**
     * 查询所有的标签
     */
    @GET("main/tag/all")
    suspend fun getAllTags(): RespResult<MutableList<Tag>>

    /**
     * 保存用户订阅标签
     */
    @POST("main/tag/user/save")
    suspend fun saveUserTags(@Body tagIds: String): RespResult<Any>

}