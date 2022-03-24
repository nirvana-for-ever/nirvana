package com.nirvana.blog.entity.ui.user

/**
 * 基础用户信息
 */
data class SimpleUserInfo(
    val nickName: String,
    val photo: String,
    /**
     * TODO 等之后关注模块做了再整这个值，暂时都设置为 0
     */
    val subsribeCount: Int,
    /**
     * TODO 等之后关注模块做了再整这个值，暂时都设置为 0
     */
    val fansCount: Int,
    /**
     * 发布文章数
     */
    val articleCount: Int,
    /**
     * 草稿数
     */
    val draftCount: Int,
    /**
     * 点赞的文章数（不是被点赞，是自己点赞别人）
     */
    val likeCount: Int,
    /**
     * 牛币数量
     * TODO
     */
    val nbCount: Float
)