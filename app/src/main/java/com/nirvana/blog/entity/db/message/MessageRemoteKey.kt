package com.nirvana.blog.entity.db.message

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MessageRemoteKey(
    @PrimaryKey
    val flag: Int,
    val lastId: String
)
