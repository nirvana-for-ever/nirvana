package com.nirvana.blog.adapter.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nirvana.blog.adapter.BaseViewBindingAdapter
import com.nirvana.blog.adapter.BaseViewBindingViewHolder
import com.nirvana.blog.databinding.UserOptionsViewItemBinding
import com.nirvana.blog.entity.ui.user.UserOption

/**
 * 我的页面中，用户下方选项菜单 rv 适配器
 */
class UserOptionsRecyclerViewAdapter(list: MutableList<UserOption>) :
    BaseViewBindingAdapter<UserOption, UserOptionsViewItemBinding, BaseViewBindingViewHolder<UserOptionsViewItemBinding>>(list) {

    override fun bind(parent: ViewGroup): BaseViewBindingViewHolder<UserOptionsViewItemBinding> =
        BaseViewBindingViewHolder(
            UserOptionsViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun convert(
        holder: BaseViewBindingViewHolder<UserOptionsViewItemBinding>,
        item: UserOption
    ) {
        if (item.subtitle.isBlank()) holder.binding.userOptionsSubtitle.visibility = View.GONE
        holder.binding.option = item
        if (item.onClick != null) {
            holder.binding.root.setOnClickListener(item.onClick)
        }
    }
}