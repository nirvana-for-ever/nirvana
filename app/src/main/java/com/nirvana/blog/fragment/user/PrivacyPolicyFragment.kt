package com.nirvana.blog.fragment.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.nirvana.blog.base.BaseFragment
import com.nirvana.blog.databinding.FragmentPrivacyPolicyBinding
import com.nirvana.blog.databinding.FragmentUserProtocolBinding
import com.nirvana.blog.utils.StatusBarUtils.setBaseStatusBar

class PrivacyPolicyFragment : BaseFragment<FragmentPrivacyPolicyBinding>() {

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentPrivacyPolicyBinding.inflate(inflater, container, false)

    override fun initView() {
        setBaseStatusBar {
            titleBar(binding.privacyPolicyToolbar)
        }

        binding.privacyPolicyContent.loadUrl("file:///android_asset/privacy_policy.html")

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}