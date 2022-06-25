package com.nirvana.blog.api.message

import com.nirvana.blog.entity.network.RespResult
import com.nirvana.blog.entity.ui.message.MessageComment
import com.nirvana.blog.entity.ui.message.MessageLike
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MessageServiceApi {

    /**
     * 获取评论消息
     */
    @GET("main/message/comment")
    suspend fun getMessageComment(
        @Query("clientTime") clientTime: String,
        @Query("pageSize") pageSize: Int,
        @Query("lastId") lastId: String?
    ): RespResult<List<MessageComment>>

    /**
     * 获取点赞消息
     */
    @GET("main/message/like")
    suspend fun getMessageLike(
        @Query("clientTime") clientTime: String,
        @Query("pageSize") pageSize: Int,
        @Query("lastId") lastId: String?
    ): RespResult<List<MessageLike>>

}