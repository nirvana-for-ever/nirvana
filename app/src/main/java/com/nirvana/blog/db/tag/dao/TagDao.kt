package com.nirvana.blog.db.tag.dao

import androidx.room.*
import com.nirvana.blog.entity.db.tag.TagEntity

@Dao
interface TagDao {

    /**
     * 添加未登录的标签库
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTags(tags: List<TagEntity>)

    /**
     * 获取未登录的标签库，按照顺序获取
     */
    @Query("SELECT * FROM TagEntity Order By `order` ASC")
    suspend fun selectTagsByOrder(): List<TagEntity>

    @Query("DELETE FROM TagEntity")
    suspend fun deleteTags()

}