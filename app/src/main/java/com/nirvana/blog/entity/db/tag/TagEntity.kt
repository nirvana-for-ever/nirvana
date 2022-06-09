package com.nirvana.blog.entity.db.tag

import androidx.room.Entity

@Entity(primaryKeys = ["tagId"])
data class TagEntity(val tagId: String, val tagName: String, val description: String, val hot: Long, var order: Int)
