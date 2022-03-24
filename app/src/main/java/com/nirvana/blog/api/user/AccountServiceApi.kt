package com.nirvana.blog.api.user

import com.nirvana.blog.entity.network.RespResult
import com.nirvana.blog.entity.network.user.LoginUserVo
import com.nirvana.blog.entity.ui.user.SimpleUserInfo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountServiceApi {

    /**
     * 登录
     */
    @POST("sys/login")
    suspend fun login(@Body vo: LoginUserVo): RespResult<Any>

    /**
     * 登出
     */
    @GET("sys/logout")
    suspend fun logout(): RespResult<Any>

    /**
     * 获取基础用户信息
     */
    @GET("sys/info/{type}")
    suspend fun info(@Path("type") type: Int) : RespResult<SimpleUserInfo>

    /**
     * TODO 获取复杂的用户信息
     */

}