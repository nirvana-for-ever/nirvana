package com.nirvana.blog.adapter.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nirvana.blog.adapter.BaseViewBindingAdapter
import com.nirvana.blog.adapter.BaseViewBindingViewHolder
import com.nirvana.blog.databinding.UserSettingsViewItemBinding
import com.nirvana.blog.databinding.UserSubSettingsViewItemBinding
import com.nirvana.blog.entity.ui.user.SettingOption
import com.nirvana.blog.entity.ui.user.SubSettingOption

class SubSettingOptionsRecyclerViewAdapter(private val options: MutableList<SubSettingOption>) :
    BaseViewBindingAdapter<SubSettingOption, UserSubSettingsViewItemBinding, BaseViewBindingViewHolder<UserSubSettingsViewItemBinding>>(options) {

    override fun bind(parent: ViewGroup): BaseViewBindingViewHolder<UserSubSettingsViewItemBinding> {
        return BaseViewBindingViewHolder(
            UserSubSettingsViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun convert(
        holder: BaseViewBindingViewHolder<UserSubSettingsViewItemBinding>,
        item: SubSettingOption
    ) {
        holder.binding.apply {
            subSettingsTitle.text = item.title
            if (item.subtitle.isNotBlank()) {
                subSettingsSubTitle.text = item.subtitle
            } else {
                subSettingsSubTitle.visibility = View.GONE
            }
            if (options.last() == item) {
                subSettingsDiv.visibility = View.GONE
            }
        }
    }
}