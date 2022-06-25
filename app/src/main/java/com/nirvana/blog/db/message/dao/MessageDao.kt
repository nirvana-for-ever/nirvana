package com.nirvana.blog.db.message.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nirvana.blog.entity.db.article.ArticleInfoRemoteKey
import com.nirvana.blog.entity.db.message.MessageRemoteKey

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(remoteKey: MessageRemoteKey)

    @Query("DELETE FROM MessageRemoteKey WHERE flag = :flag")
    suspend fun deleteRemoteKey(flag: Int)

    @Query("SELECT * FROM MessageRemoteKey WHERE flag = :flag")
    suspend fun selectRemoteKey(flag: Int): MessageRemoteKey?
}