package com.nirvana.blog.entity.ui.article

data class Article(
    val articleId: String,
    val authId: String,
    val authName: String,
    val authAvatar: String,
    val title: String,
    val readCount: Long,
    val commentCount: Long,
    val contentMd: String,
    val createTime: String,
    var likeCount: Long,
    var amILike: Boolean
)
