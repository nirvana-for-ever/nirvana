package com.nirvana.blog.entity.network.user

/**
 * 发送登录请求的 bean
 */
data class LoginUserVo(
    /**
     * 账户，有可能是手机号或邮箱，手机号有可能是没注册的
     */
    private val account: String,

    /**
     * 密码，有可能是密码或者是验证码
     */
    private val password: String,

    /**
     * 登录方式
     */
    private val type: Int
)
