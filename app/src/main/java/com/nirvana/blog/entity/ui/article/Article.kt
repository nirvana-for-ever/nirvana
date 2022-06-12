package com.nirvana.blog.entity.ui.article

data class Article(
    val articleId: String,
    val authId: String,
    val authName: String,
    val authAvatar: String,
    val title: String,
    val readCount: Long,
    val commentCount: Long,
    val contentHtml: String,
    val createTime: String,
    val likeCount: Long,
    val amILike: Boolean
)
