package com.nirvana.blog.activity.article

import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.nirvana.blog.R
import com.nirvana.blog.base.BaseActivity
import com.nirvana.blog.databinding.ActivityArticleBinding
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

        @JvmStatic
        @BindingAdapter("content_html")
        fun authAvatar(tv: TextView, html: String?) {
            if (html != null) {
                tv.text = Html.fromHtml(html)
            }
        }
    }

    private val articleViewModel: ArticleViewModel by viewModels()

    override fun bind(): ActivityArticleBinding = ActivityArticleBinding.inflate(layoutInflater)

    override fun initView() {
        val articleId = intent.getStringExtra("articleId")
        articleViewModel.getArticle(articleId!!)
    }

    override fun initListener() {
        articleViewModel.article.observe(this) {
            binding.article = it
        }
    }
}