package com.nirvana.blog.entity.ui.message

data class MessageLike(
    val messageId: String,
    val senderAvatar: String,
    val senderName: String,
    val senderId: String,
    val acticleId: String,
    val acticleTitle: String,
    val createTime: String,
    val pId: String,
)
