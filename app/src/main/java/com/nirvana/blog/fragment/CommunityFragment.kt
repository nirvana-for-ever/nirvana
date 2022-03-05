package com.nirvana.blog.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gyf.immersionbar.ktx.immersionBar
import com.nirvana.blog.R
import com.nirvana.blog.base.BaseFragment
import com.nirvana.blog.databinding.FragmentCommunityBinding

/**
 * 搞得像虎扑那样的社区，标签
 */
class CommunityFragment : BaseFragment<FragmentCommunityBinding>() {

    companion object {
        @JvmStatic
        fun newInstance() = CommunityFragment()
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentCommunityBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.root
    }

}