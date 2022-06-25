package com.nirvana.blog.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.nirvana.blog.activity.message.InteractionActivity
import com.nirvana.blog.base.BaseFragment
import com.nirvana.blog.databinding.FragmentMessageBinding
import com.nirvana.blog.utils.StatusBarUtils.setBaseStatusBar
import com.nirvana.blog.utils.isLogin
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : BaseFragment<FragmentMessageBinding>() {

    companion object {
        @JvmStatic
        fun newInstance() = MessageFragment()
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMessageBinding.inflate(inflater, container, false)

    override fun onResume() {
        super.onResume()
        setBaseStatusBar {
            titleBar(binding.messageTitleBar)
        }
    }

    override fun initView() {
        binding.root
    }

    override fun initObserver() {
        binding.messageInteractionBtn.setOnClickListener {
            if (isLogin) {
                startActivity(Intent(context, InteractionActivity::class.java))
            }
        }
    }

}