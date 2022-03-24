package com.nirvana.blog.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.nirvana.blog.adapter.BaseViewBindingAdapter
import com.nirvana.blog.adapter.BaseViewBindingViewHolder
import com.nirvana.blog.databinding.UserSettingsViewItemBinding
import com.nirvana.blog.entity.ui.user.SettingOption

class SettingOptionsRecyclerViewAdapter(options: MutableList<SettingOption>) :
    BaseViewBindingAdapter<SettingOption, UserSettingsViewItemBinding, BaseViewBindingViewHolder<UserSettingsViewItemBinding>>(options) {

    override fun bind(parent: ViewGroup): BaseViewBindingViewHolder<UserSettingsViewItemBinding> {
        return BaseViewBindingViewHolder(
            UserSettingsViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun convert(
        holder: BaseViewBindingViewHolder<UserSettingsViewItemBinding>,
        item: SettingOption
    ) {
        holder.binding.apply {
            settingsTitle.text = item.title
            subSettingsRv.apply {
                adapter = SubSettingOptionsRecyclerViewAdapter(item.subOptions)
                layoutManager = LinearLayoutManager(context)
            }
        }
    }
}