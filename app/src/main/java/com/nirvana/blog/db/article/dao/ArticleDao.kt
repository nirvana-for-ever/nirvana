package com.nirvana.blog.db.article.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nirvana.blog.entity.db.article.ArticleInfoEntity

/**
 * 博客的所有数据库查询
 */
@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticleInfo(articleInfos: List<ArticleInfoEntity>)

    @Query("DELETE FROM ArticleInfoEntity WHERE tagId = :tagId")
    suspend fun clearArticleInfos(tagId: String)

    /**
     * 提供获取所有数据的SQL，分页的操作由Room帮我们完成
     */
    @Query("SELECT * FROM ArticleInfoEntity WHERE tagId = :tagId")
    fun articleInfoPagingSource(tagId: String): PagingSource<Int, ArticleInfoEntity>

}
