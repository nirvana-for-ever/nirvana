package com.nirvana.blog.activity.article

import android.view.View
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.widget.NestedScrollView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.nirvana.blog.R
import com.nirvana.blog.base.BaseActivity
import com.nirvana.blog.databinding.ActivityArticleBinding
import com.nirvana.blog.entity.ui.article.Article
import com.nirvana.blog.utils.Constants
import com.nirvana.blog.utils.MarkwonUtils
import com.nirvana.blog.utils.StatusBarUtils.setBaseStatusBar
import com.nirvana.blog.utils.isLogin
import com.nirvana.blog.utils.toastShort
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

    private var isLikeRequestSending = false

    override fun bind(): ActivityArticleBinding = ActivityArticleBinding.inflate(layoutInflater)

    override fun initStatusBar() {
        setBaseStatusBar()
    }

    override fun initView() {
        val articleId = intent.getStringExtra("articleId")
        articleViewModel.getArticle(articleId!!)
    }

    override fun initListener() {
        // 滑动监听，设置上方标题栏的内容是否可见
        binding.articleScroll.apply {
            setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
                // scrollY 为总滑动距离
                if (scrollY != 0 && binding.articleColAvatar.visibility == View.INVISIBLE) {
                    binding.articleColAvatar.visibility = View.VISIBLE
                    binding.articleColAuth.visibility = View.VISIBLE
                    binding.articleColSubscribeBtn.visibility = View.VISIBLE
                } else if (scrollY == 0 && binding.articleColAvatar.visibility == View.VISIBLE) {
                    binding.articleColAvatar.visibility = View.INVISIBLE
                    binding.articleColAuth.visibility = View.INVISIBLE
                    binding.articleColSubscribeBtn.visibility = View.INVISIBLE
                }
            })
        }

        // 获取文章详情监听
        articleViewModel.article.observe(this) {
            binding.article = it

            // 根据是否点赞设置点赞按钮
            updateLikeStatus(it.amILike)

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

        // 点赞
        binding.articleLike.setOnClickListener {
            likeOrUnlike()
        }
        // 取消点赞
        binding.articleLikeFill.setOnClickListener {
            likeOrUnlike()
        }

        // 返回
        binding.articleColBack.setOnClickListener {
            finish()
        }
    }

    private fun likeOrUnlike() {
        if (!isLogin) {
            toastShort(R.string.article_like_unlogin_warning_string)
        } else if (!isLikeRequestSending) {
            isLikeRequestSending = true
            val article: Article = binding.article!!
            articleViewModel.likeArticle(article.articleId, article.amILike) {
                if (it) {
                    if (article.amILike) {
                        toastShort("取消点赞成功")
                        updateLikeStatus(false)
                        article.likeCount = article.likeCount - 1
                    } else {
                        toastShort("点赞成功")
                        updateLikeStatus(true)
                        article.likeCount = article.likeCount + 1
                    }
                    article.amILike = !article.amILike
                    binding.article = binding.article
                } else {
                    toastShort(Constants.SERVER_ERROR_MSG)
                }
                isLikeRequestSending = false
            }
        }
    }

    private fun updateLikeStatus(like: Boolean) {
        if (like) {
            binding.articleLike.visibility = View.GONE
            binding.articleLikeFill.visibility = View.VISIBLE
        } else {
            binding.articleLike.visibility = View.VISIBLE
            binding.articleLikeFill.visibility = View.GONE
        }
    }
}