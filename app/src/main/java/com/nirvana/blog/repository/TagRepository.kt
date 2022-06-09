package com.nirvana.blog.repository

import com.nirvana.blog.api.tag.TagServiceApi
import com.nirvana.blog.base.BaseRepository
import com.nirvana.blog.db.tag.TagDataBase
import com.nirvana.blog.entity.ui.tag.Tag
import com.nirvana.blog.mapper.tag.TagMapper

class TagRepository(
    private val api: TagServiceApi,
    private val tagDataBase: TagDataBase
) : BaseRepository() {

    suspend fun getTags() = safeReq { api.getTags() }

    suspend fun saveTags2DB(tags: MutableList<Tag>) = safeDB {
        tagDataBase.getTagDao().apply {
            // 先删后插
            deleteTags()
            insertTags(tags.mapIndexed { index, tag ->
                TagMapper.reverseMap(tag, index)
            })
        }
    }

    suspend fun getDBTags() =
        safeDB { tagDataBase.getTagDao().selectTagsByOrder().map { TagMapper.map(it) }}

    suspend fun getAllTags() = safeReq { api.getAllTags() }

    suspend fun saveUserTags(tagIds: String) = safeReq { api.saveUserTags(tagIds) }

}