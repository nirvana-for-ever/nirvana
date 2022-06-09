package com.nirvana.blog.db.article.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nirvana.blog.entity.db.article.ArticleInfoRemoteKey

@Dao
interface ArticleInfoRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(remoteKey: ArticleInfoRemoteKey)

    @Query("DELETE FROM ArticleInfoRemoteKey WHERE tagId = :tagId")
    suspend fun deleteRemoteKey(tagId: String)

    @Query("SELECT * FROM ArticleInfoRemoteKey WHERE tagId = :tagId")
    suspend fun selectRemoteKey(tagId: String): ArticleInfoRemoteKey?

}
