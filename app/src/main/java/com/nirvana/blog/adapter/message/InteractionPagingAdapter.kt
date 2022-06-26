package com.nirvana.blog.adapter.message

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.nirvana.blog.R
import com.nirvana.blog.adapter.BaseViewBindingViewHolder
import com.nirvana.blog.databinding.MessageInteractionCommentItemBinding
import com.nirvana.blog.databinding.MessageInteractionLikeItemBinding
import com.nirvana.blog.entity.ui.message.MessageComment
import com.nirvana.blog.entity.ui.message.MessageLike

class InteractionPagingAdapter :
    PagingDataAdapter<Any, BaseViewBindingViewHolder<ViewBinding>>(InteractionDiffCallback()) {

    companion object {
        @JvmStatic
        @BindingAdapter("sender_avatar")
        fun senderAvatar(iv: ImageView, url: String) {
            Glide.with(iv.context)
                .load(url)
                .placeholder(R.drawable.ic_loading_img)
                .transform(CircleCrop())
                .into(iv)
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewBindingViewHolder<ViewBinding>,
        position: Int
    ) {
        when (holder.binding) {
            is MessageInteractionCommentItemBinding -> {
                holder.binding.comment = getItem(position) as MessageComment?
            }
            is MessageInteractionLikeItemBinding -> {
                holder.binding.like = getItem(position) as MessageLike?
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewBindingViewHolder<ViewBinding> {
        return BaseViewBindingViewHolder(
            when (viewType) {
                0 -> {
                    MessageInteractionCommentItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                }
                1 -> {
                    MessageInteractionLikeItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                }
                else -> throw RuntimeException()
            }
        )
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is MessageComment -> 0
            is MessageLike -> 1
            else -> throw RuntimeException()
        }
    }

}

class InteractionDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when (oldItem) {
            is MessageComment -> oldItem.acticleId == (newItem as MessageComment).acticleId
            is MessageLike -> oldItem.acticleId == (newItem as MessageComment).acticleId
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when (oldItem) {
            is MessageComment -> {
                val old: MessageComment = oldItem
                val new = (newItem as MessageComment)
                old == new
            }
            is MessageLike -> {
                val old: MessageLike = oldItem
                val new = (newItem as MessageLike)
                old == new
            }
            else -> false
        }
    }
}
