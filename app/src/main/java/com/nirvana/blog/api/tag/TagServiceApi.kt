package com.nirvana.blog.api.tag

import com.nirvana.blog.entity.ui.tag.Tag
import com.nirvana.blog.entity.network.RespResult
import retrofit2.http.GET

interface TagServiceApi {

    /**
     * 查询所有的标签
     */
    @GET("main/getTags")
    suspend fun getTags(): RespResult<List<Tag>>

}