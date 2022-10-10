package com.nirvana.blog.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import com.gyf.immersionbar.ktx.immersionBar
import com.nirvana.blog.R
import com.nirvana.blog.base.BaseFragment
import com.nirvana.blog.databinding.FragmentCommunityBinding
import kotlinx.android.synthetic.main.fragment_community.*

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
        initWebView()
    }

    private fun initWebView(){
        bwv_community_content.settings.javaScriptEnabled = true
        bwv_community_content.settings.setAppCacheMaxSize(1024*1024*8)
        bwv_community_content.settings.setAppCachePath(activity?.cacheDir?.absolutePath)
        bwv_community_content.settings.setAppCacheEnabled(true)
        bwv_community_content.settings.allowFileAccess = true
        bwv_community_content.settings.setSupportZoom(false)
        bwv_community_content.settings.builtInZoomControls = true
        bwv_community_content.settings.displayZoomControls = true
        bwv_community_content.settings.blockNetworkImage = false
        bwv_community_content.settings.loadsImagesAutomatically = true
        bwv_community_content.settings.defaultTextEncodingName = "utf-8"
        bwv_community_content.settings.useWideViewPort = true
        bwv_community_content.settings.loadWithOverviewMode = true
        bwv_community_content.settings.setAppCacheEnabled(true)
        bwv_community_content.settings.domStorageEnabled = true
        bwv_community_content.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        bwv_community_content.settings.useWideViewPort = true

        bwv_community_content.loadUrl("https://nirvana1234.xyz/index/record")
    }

}