package com.nirvana.blog.activity.article

import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.nirvana.blog.R
import com.nirvana.blog.base.BaseActivity
import com.nirvana.blog.databinding.ActivityArticleBinding
import com.nirvana.blog.utils.MarkwonUtils
import com.nirvana.blog.utils.StatusBarUtils.setBaseStatusBar
import com.nirvana.blog.viewmodel.article.ArticleViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleActivity : BaseActivity<ActivityArticleBinding>() {

    companion object {
        @JvmStatic
        @BindingAdapter("article_auth_avatar")
        fun authAvatar(iv: ImageView, url: String?) {
            if (url != null) {
                Glide.with(iv.context)
                    .load(url)
                    .placeholder(R.drawable.ic_loading_img)
                    .transform(CircleCrop())
                    .into(iv)
            }
        }
    }

    private val articleViewModel: ArticleViewModel by viewModels()

    override fun bind(): ActivityArticleBinding = ActivityArticleBinding.inflate(layoutInflater)

    override fun initStatusBar() {
        setBaseStatusBar {
            titleBar(binding.articleTitleBar)
        }
    }

    override fun initView() {
        val articleId = intent.getStringExtra("articleId")
        articleViewModel.getArticle(articleId!!)
    }

    override fun initListener() {
        articleViewModel.article.observe(this) {
            binding.article = it

            // 设置 md
            val markwon = MarkwonUtils.basicMarkwon(this)
            val markwonAdapter = MarkwonUtils.basicAdapter()

            binding.articleMarkwonRv.apply {
                layoutManager = LinearLayoutManager(this@ArticleActivity)
                adapter = markwonAdapter
            }

            markwonAdapter.setMarkdown(markwon, it.contentMd)
            markwonAdapter.notifyDataSetChanged()
        }
    }
}