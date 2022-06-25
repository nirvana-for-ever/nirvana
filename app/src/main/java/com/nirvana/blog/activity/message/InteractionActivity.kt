package com.nirvana.blog.activity.message

import com.google.android.material.tabs.TabLayoutMediator
import com.nirvana.blog.adapter.BaseFragmentViewPagerAdapter
import com.nirvana.blog.base.BaseActivity
import com.nirvana.blog.databinding.ActivityInteractionBinding
import com.nirvana.blog.fragment.message.InteractionFragment
import com.nirvana.blog.utils.Constants
import com.nirvana.blog.utils.StatusBarUtils.setBaseStatusBar
import com.nirvana.blog.utils.setNormalSensitivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InteractionActivity : BaseActivity<ActivityInteractionBinding>() {

    private val tabList = listOf("评论", "赞", "关注")

    private lateinit var vpAdapter: BaseFragmentViewPagerAdapter

    override fun bind(): ActivityInteractionBinding =
        ActivityInteractionBinding.inflate(layoutInflater)

    override fun initStatusBar() {
        setBaseStatusBar {
            titleBar(binding.messageInteractionTitleBar)
        }
    }

    override fun initView() {
        vpAdapter = BaseFragmentViewPagerAdapter(
            supportFragmentManager, lifecycle, listOf(
                { InteractionFragment.newInstance(Constants.MESSAGE_INTERACTION_COMMENT_TYPE) },
                { InteractionFragment.newInstance(Constants.MESSAGE_INTERACTION_LIKE_TYPE) },
                { InteractionFragment.newInstance(Constants.MESSAGE_INTERACTION_SUBSCRIBE_TYPE) },
            )
        )
        binding.messageInteractionVp.apply {
            adapter = vpAdapter
            setNormalSensitivity()
        }

        TabLayoutMediator(
            binding.messageInteractionTab, binding.messageInteractionVp
        ) { tab, position ->
            tab.text = tabList[position]
        }.attach()
    }

}