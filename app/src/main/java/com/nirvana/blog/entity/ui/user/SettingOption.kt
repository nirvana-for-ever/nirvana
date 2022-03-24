package com.nirvana.blog.entity.ui.user

data class SettingOption(val title: String, val subOptions: MutableList<SubSettingOption>)

data class SubSettingOption(val title: String, val subtitle: String = "")