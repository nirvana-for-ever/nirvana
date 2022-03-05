package com.nirvana.blog.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 远程键对象，用于存储下一页的页码
 */
@Entity
data class ArticleInfoRemoteKey(
    @PrimaryKey
    val tagId: String,
    val nextPage: Int
)
