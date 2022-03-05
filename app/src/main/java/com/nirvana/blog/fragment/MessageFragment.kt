package com.nirvana.blog.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gyf.immersionbar.ktx.immersionBar
import com.nirvana.blog.R
import com.nirvana.blog.base.BaseFragment
import com.nirvana.blog.databinding.FragmentMessageBinding

class MessageFragment : BaseFragment<FragmentMessageBinding>() {

    companion object {
        @JvmStatic
        fun newInstance() = MessageFragment()
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMessageBinding.inflate(inflater, container, false)

    override fun initView() {
        binding.root
    }

}