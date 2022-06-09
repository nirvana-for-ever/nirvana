package com.nirvana.blog.adapter.tag

import android.view.LayoutInflater
import android.view.ViewGroup
import com.nirvana.blog.adapter.BaseViewBindingAdapter
import com.nirvana.blog.adapter.BaseViewBindingViewHolder
import com.nirvana.blog.databinding.IndexTagSettingItemBinding
import com.nirvana.blog.entity.ui.tag.Tag

class IndexTagSettingUnchosenRecyclerViewAdapter(
    private val tags: MutableList<Tag>,
    private val onChange: (Tag) -> Unit
) : BaseViewBindingAdapter<Tag, IndexTagSettingItemBinding, BaseViewBindingViewHolder<IndexTagSettingItemBinding>>(tags) {

    init {
        setDiffCallback(IndexTagsDiffCallback())
    }

    override fun bind(parent: ViewGroup): BaseViewBindingViewHolder<IndexTagSettingItemBinding> =
        BaseViewBindingViewHolder(
            IndexTagSettingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun convert(holder: BaseViewBindingViewHolder<IndexTagSettingItemBinding>, item: Tag) {
        holder.binding.apply {
            tag = item
            indexTagSettingItemBtn.setOnClickListener {
                val removePos = tags.indexOf(item)
                onChange(item)
                notifyItemRemoved(removePos)
            }
        }
    }

}