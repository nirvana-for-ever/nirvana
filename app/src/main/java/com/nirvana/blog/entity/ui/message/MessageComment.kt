package com.nirvana.blog.entity.ui.message

data class MessageComment(
    val messageId: String,
    val senderAvatar: String,
    val senderName: String,
    val senderId: String,
    val content: String,
    val acticleId: String,
    val acticleTitle: String,
    val createTime: String,
    /**
     * 二级评论才有用，表示一级评论的 id
     * 空串就表示当前评论是一级评论
     */
    val pId: String,
)
