package com.nirvana.blog.activity.user

import com.nirvana.blog.base.BaseActivity
import com.nirvana.blog.databinding.ActivityAccountBinding
import com.nirvana.blog.utils.StatusBarUtils.setBaseStatusBar

class AccountActivity : BaseActivity<ActivityAccountBinding>() {

    override fun bind(): ActivityAccountBinding = ActivityAccountBinding.inflate(layoutInflater)

    override fun initStatusBar() {
        setBaseStatusBar()
    }
}