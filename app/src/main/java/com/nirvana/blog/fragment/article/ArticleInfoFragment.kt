package com.nirvana.blog.fragment.article

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewpager2.widget.ViewPager2
import com.nirvana.blog.R
import com.nirvana.blog.adapter.article.IndexArticleInfoPagingAdapter
import com.nirvana.blog.base.BaseFragment
import com.nirvana.blog.databinding.FragmentArticleInfoBinding
import com.nirvana.blog.fragment.IndexFragment
import com.nirvana.blog.utils.DensityUtils
import com.nirvana.blog.viewmodel.article.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleInfoFragment : BaseFragment<FragmentArticleInfoBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(
            // 每个 tagId 对应一个 Fragment
            tagId: String
        ): ArticleInfoFragment {
            return ArticleInfoFragment().apply {
                arguments = Bundle().apply {
                    putString("tagId", tagId)
                }
            }
        }
    }

    private lateinit var tagId: String

    private val articleInfoPagingAdapter = IndexArticleInfoPagingAdapter()

    /**
     * 创建 articleViewModel 时，需要单例，可以通过设置 ownerProducer 为它们的父fragment，也就是 IndexFragment
     * 这样创建多个 ArticleInfoFragment 时，发现 ownerProducer 都是一样的，就不会创建多个 ViewModel
     * 具体为什么可以保证可以看 jetpack 笔记中 ViewModel 中：如何保证同一个view不会多次创建viewmodel
     */
    private val articleViewModel: ArticleViewModel by viewModels(ownerProducer = { requireParentFragment() })

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentArticleInfoBinding =
        FragmentArticleInfoBinding.inflate(inflater, container, false)

    override fun initViewLazy() {
        // 设置标签id，从 newInstance 中传递过来的
        tagId = arguments?.getString("tagId").toString()

        binding.articleInfoRv.apply {
            adapter = articleInfoPagingAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ArticleInfoItemDecoration())
        }
    }

    override fun initObserverLazy() {
        // 获取文章信息
        articleViewModel.articleInfoLiveData(tagId).observe(this) {
            articleInfoPagingAdapter.submitData(lifecycle, it)
        }
    }

    inner class ArticleInfoItemDecoration : ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.set(0, 0, 0, resources.getDimensionPixelOffset(R.dimen.index_article_info_rv_divider_height))
        }
    }

}