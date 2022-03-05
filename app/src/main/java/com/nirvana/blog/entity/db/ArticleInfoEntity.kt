package com.nirvana.blog.entity.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 存储在数据库中的对象，nextPage 在本例中可以不需要，其实跟展示在UI的对象是一致的
 * 但是在实际情况中可能会不同，最好分开
 */
@Entity(primaryKeys = ["articleId", "tagId"])// 复合主键，每一个标签对应的数据都需要独立，所以标签id也应该是主键
data class ArticleInfoEntity(
    val articleId: String,
    val authorId: String,
    val author: String,
    val authorAvatar: String,
    val title: String,
    val content: String?,
    val img1: String?,
    val img2: String?,
    val img3: String?,
    val readCount: String,
    val commentCount: String,
    val likeCount: String,

    // 标签的id，由于首页的博客会根据标签分成不同的 viewPager，所以需要多一个标签id的属性
    val tagId: String
)
