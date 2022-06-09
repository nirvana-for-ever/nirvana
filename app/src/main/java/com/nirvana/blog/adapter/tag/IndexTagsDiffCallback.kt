package com.nirvana.blog.adapter.tag

import androidx.recyclerview.widget.DiffUtil
import com.nirvana.blog.entity.ui.tag.Tag

class IndexTagsDiffCallback : DiffUtil.ItemCallback<Tag>() {
    override fun areItemsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return oldItem.tagId == newItem.tagId
    }
    // Tag 一样就肯定是同一个 Tag
    override fun areContentsTheSame(oldItem: Tag, newItem: Tag): Boolean {
        return true
    }
}