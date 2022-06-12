package com.nirvana.blog.adapter.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.nirvana.blog.R
import com.nirvana.blog.adapter.BaseViewBindingViewHolder
import com.nirvana.blog.databinding.IndexArticleInfo0imgBinding
import com.nirvana.blog.databinding.IndexArticleInfo1imgBinding
import com.nirvana.blog.databinding.IndexArticleInfo3imgBinding
import com.nirvana.blog.entity.ui.article.ArticleInfo
import com.nirvana.blog.entity.ui.article.diffBundleFrom
import com.nirvana.blog.entity.ui.article.updateBundleFrom
import com.nirvana.blog.view.RoundCornerImageView

class IndexArticleInfoPagingAdapter(
    private val toArticlePageCallback: (String) -> Unit
) : PagingDataAdapter<ArticleInfo, BaseViewBindingViewHolder<ViewDataBinding>>(
    ArticleInfoDiffCallback()
) {
    companion object {
        @JvmStatic
        @BindingAdapter("author_avatar")
        fun authorAvatar(iv: RoundCornerImageView, url: String) {
            Glide.with(iv.context)
                .load(url)
                .placeholder(R.drawable.ic_loading_img)
                .transform(CircleCrop())
                .into(iv)
        }
        @JvmStatic
        @BindingAdapter("img3_1")
        fun fstOfImg3(iv: RoundCornerImageView, url: String) {
            iv.setRadius(leftTop = 5f, leftBottom = 5f, dp = true)
            Glide.with(iv.context)
                .load(url)
                .placeholder(R.drawable.ic_loading_img)
                .error(R.drawable.ic_loading_img_fail)
                .into(iv)
        }
        @JvmStatic
        @BindingAdapter("img3_2")
        fun secOfImg3(iv: RoundCornerImageView, url: String) {
            Glide.with(iv.context)
                .load(url)
                .placeholder(R.drawable.ic_loading_img)
                .error(R.drawable.ic_loading_img_fail)
                .into(iv)
        }
        @JvmStatic
        @BindingAdapter("img3_3")
        fun thdOfImg3(iv: RoundCornerImageView, url: String) {
            iv.setRadius(rightTop = 5f, rightBottom = 5f, dp = true)
            Glide.with(iv.context)
                .load(url)
                .placeholder(R.drawable.ic_loading_img)
                .error(R.drawable.ic_loading_img_fail)
                .into(iv)
        }
        @JvmStatic
        @BindingAdapter("img1")
        fun img1(iv: RoundCornerImageView, url: String) {
            iv.setRadius(radius = 5f, dp = true)
            Glide.with(iv.context)
                .load(url)
                .placeholder(R.drawable.ic_loading_img)
                .error(R.drawable.ic_loading_img_fail)
                .into(iv)
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewBindingViewHolder<ViewDataBinding>,
        position: Int
    ) {
        when (holder.binding) {
            is IndexArticleInfo3imgBinding -> {
                holder.binding.articleInfo = getItem(position)
                holder.binding.root.setOnClickListener {
                    // 跳转到对应的博客详情页
                    toArticlePageCallback(holder.binding.articleInfo!!.articleId)
                }
            }
            is IndexArticleInfo1imgBinding -> {
                holder.binding.articleInfo = getItem(position)
                holder.binding.root.setOnClickListener {
                    // 跳转到对应的博客详情页
                    toArticlePageCallback(holder.binding.articleInfo!!.articleId)
                }
            }
            is IndexArticleInfo0imgBinding -> {
                holder.binding.articleInfo = getItem(position)
                holder.binding.root.setOnClickListener {
                    // 跳转到对应的博客详情页
                    toArticlePageCallback(holder.binding.articleInfo!!.articleId)
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewBindingViewHolder<ViewDataBinding>,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            when (holder.binding) {
                is IndexArticleInfo3imgBinding ->
                    holder.binding.articleInfo!!.updateBundleFrom(getItem(position)!!, payloads[0] as Bundle)
                is IndexArticleInfo1imgBinding ->
                    holder.binding.articleInfo!!.updateBundleFrom(getItem(position)!!, payloads[0] as Bundle)
                is IndexArticleInfo0imgBinding ->
                    holder.binding.articleInfo!!.updateBundleFrom(getItem(position)!!, payloads[0] as Bundle)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewBindingViewHolder<ViewDataBinding> =
        BaseViewBindingViewHolder(
            when (viewType) {
                0 -> {
                    // 3 张图片的文章简介
                    IndexArticleInfo3imgBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                }
                1 -> {
                    // 1 张图片的文章简介
                    IndexArticleInfo1imgBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                }
                2 -> {
                    // 没有图片的文章简介
                    IndexArticleInfo0imgBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                }
                else -> throw RuntimeException()
            }
        )

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)!!
        return when {
            item.content == null -> 0
            item.img1 != null -> 1
            else -> 2
        }
    }

}

class ArticleInfoDiffCallback : DiffUtil.ItemCallback<ArticleInfo>() {
    override fun areItemsTheSame(oldItem: ArticleInfo, newItem: ArticleInfo): Boolean {
        return oldItem.articleId == newItem.articleId
    }

    override fun areContentsTheSame(oldItem: ArticleInfo, newItem: ArticleInfo): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: ArticleInfo, newItem: ArticleInfo): Any? {
        return newItem.diffBundleFrom(oldItem)
    }
}
