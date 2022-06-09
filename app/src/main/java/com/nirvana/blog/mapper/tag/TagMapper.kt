package com.nirvana.blog.mapper.tag

import com.nirvana.blog.entity.db.tag.TagEntity
import com.nirvana.blog.entity.ui.tag.Tag
import com.nirvana.blog.mapper.BaseMapper

object TagMapper : BaseMapper<TagEntity, Tag>() {
    override fun map(input: TagEntity, vararg args: Any): Tag {
        return Tag(input.tagId, input.tagName, input.description, input.hot)
    }

    override fun reverseMap(output: Tag, vararg args: Any): TagEntity {
        return TagEntity(output.tagId, output.tagName, output.description, output.hot,
            args[0] as Int
        )
    }
}