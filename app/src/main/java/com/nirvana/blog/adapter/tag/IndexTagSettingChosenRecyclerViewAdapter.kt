package com.nirvana.blog.adapter.tag

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nirvana.blog.adapter.BaseViewBindingAdapter
import com.nirvana.blog.adapter.BaseViewBindingViewHolder
import com.nirvana.blog.databinding.IndexTagSettingItemBinding
import com.nirvana.blog.entity.ui.tag.Tag

class IndexTagSettingChosenRecyclerViewAdapter(
    val tags: MutableList<Tag>,
    private val onChange: (Tag) -> Unit,
    private val startDrag: (BaseViewBindingViewHolder<IndexTagSettingItemBinding>) -> Unit
) : BaseViewBindingAdapter<Tag, IndexTagSettingItemBinding, BaseViewBindingViewHolder<IndexTagSettingItemBinding>>(
    tags
) {

    init {
        setDiffCallback(IndexTagsDiffCallback())
    }

    private val tagBindings: MutableList<IndexTagSettingItemBinding> = mutableListOf()

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
            tagBindings.add(this)
            tag = item
            indexTagSettingItemRemoveImg.setOnClickListener {
                if (!item.tagId.startsWith("-")) {
                    val removePos = tags.indexOf(item)
                    onChange(item)
                    notifyItemRemoved(removePos)
                }
            }
            if (!item.tagId.startsWith("-")) {
                indexTagSettingItemBtn.setOnLongClickListener {
                    startDrag(holder)
                    true
                }
            }
        }
    }

    /**
     * 编辑状态改变，让右上角的叉叉显示或者消失
     */
    fun editStatusChange(editStatus: Boolean) {
        tagBindings.forEach {
            if (!it.tag!!.tagId.startsWith("-")) {
                it.indexTagSettingItemRemoveImg.visibility =
                    if (editStatus) View.VISIBLE else View.GONE
            }
        }
    }

}

