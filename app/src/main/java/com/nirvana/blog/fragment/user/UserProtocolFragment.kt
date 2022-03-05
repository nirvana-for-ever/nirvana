package com.nirvana.blog.fragment.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nirvana.blog.base.BaseFragment
import com.nirvana.blog.databinding.FragmentUserProtocolBinding
import com.nirvana.blog.utils.StatusBarUtils.setBaseStatusBar

class UserProtocolFragment : BaseFragment<FragmentUserProtocolBinding>() {

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentUserProtocolBinding.inflate(inflater, container, false)

    override fun initView() {
        setBaseStatusBar {
            titleBar(binding.userProtocolToolbar)
        }

        binding.userProtocolContent.loadUrl("file:///android_asset/user_protocol.html")

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}