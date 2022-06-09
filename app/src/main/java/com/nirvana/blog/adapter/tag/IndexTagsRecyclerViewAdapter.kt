package com.nirvana.blog.adapter.tag

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import com.nirvana.blog.R
import com.nirvana.blog.adapter.BaseViewBindingAdapter
import com.nirvana.blog.adapter.BaseViewBindingViewHolder
import com.nirvana.blog.databinding.IndexTagsButtonBinding
import com.nirvana.blog.entity.ui.tag.Tag

/**
 * 顶部栏标签的 rv
 */
class IndexTagsRecyclerViewAdapter(
    tags: MutableList<Tag>,
    private val helper: IndexTagsButtonChangeHelper
) : BaseViewBindingAdapter<Tag, IndexTagsButtonBinding, BaseViewBindingViewHolder<IndexTagsButtonBinding>>(tags) {

    init {
        setDiffCallback(IndexTagsDiffCallback())
    }

    /**
     * 绑定对应的 ViewBinding，并设置给 ViewHolder
     */
    override fun bind(parent: ViewGroup): BaseViewBindingViewHolder<IndexTagsButtonBinding> =
        BaseViewBindingViewHolder(
            IndexTagsButtonBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun convert(holder: BaseViewBindingViewHolder<IndexTagsButtonBinding>, item: Tag) {

        holder.binding.indexTagsBtn.apply {
            text = item.tagName
            if (item.tagId == helper.curTagId) {
                helper.setBtnSelected(this)
            } else {
                helper.setBtnUnselected(this)
            }
            setOnClickListener {
                helper.changeCurBtn(it as Button, item.tagId)
            }
        }
    }

    /**
     * 当 view 被回收时的回调，当被回收的 view 是被选中的标签的 view 时，需要将 helper 中 curBtn 设置为空
     * 这里说一下为什么要设置为空
     * 是为了保证 curBtn 始终是被选中的按钮对象，当被选中的按钮滑出很远之外被回收时，helper 中的 curBtn 设置为空
     * 当然其实不设置为空也不会出错，但是在 changeCurBtn 时，如果此时被选中的按钮已经被回收时
     * 就会导致 curBtn 被无意义的设置一次背景和字体颜色
     */
    override fun onViewRecycled(holder: BaseViewBindingViewHolder<IndexTagsButtonBinding>) {
        if (holder.binding.indexTagsBtn === helper.curBtn) {
            helper.curBtn = null
        }
    }

}

/**
 * 博客首页上方标签切换助手
 */
class IndexTagsButtonChangeHelper(
    // 当前标签的按钮对象，可能是 null，因为 rv 会回收按钮
    // 一旦被选中的标签对应的按钮被回收了，就会通过我们自己重写的 onViewRecycled 方法将 curBtn 设置成 null
    // 目的是保证 curBtn 始终是被选中的按钮对象
    // 但是不要紧，curTagId 会持有当前被选中的标签的id，等到 rv 滑动到使当前标签按钮重新显示会重新设置回来
    var curBtn: Button? = null,
    var curTagId: String,
    // 当切换标签时的回调事件
    val onTagsBtnChange: (String) -> Unit
) {
    init {
        onTagsBtnChange(curTagId)
    }
    fun changeCurBtn(newBtn: Button, tagId: String) {
        curBtn?.let { setBtnUnselected(it) }
        setBtnSelected(newBtn)
        onTagsBtnChange(tagId)
        curTagId = tagId
    }

    /**
     * 滑动vp改变当前标签按钮的方法，不用再进行回调
     */
    fun changeCurBtnOutside(newBtn: Button, tagId: String) {
        curBtn?.let { setBtnUnselected(it) }
        setBtnSelected(newBtn)
        curTagId = tagId
    }

    fun setBtnSelected(btn: Button) {
        btn.apply {
            background = ContextCompat.getDrawable(context, R.drawable.index_tags_btn_checked)!!
            setTextColor(ContextCompat.getColor(context, R.color.index_tags_btn_checked_text_color))
            curBtn = this
        }
    }
    fun setBtnUnselected(btn: Button) {
        btn.apply {
            background = ContextCompat.getDrawable(context, R.drawable.index_tags_btn_unchecked)!!
            setTextColor(ContextCompat.getColor(context, R.color.index_tags_btn_unchecked_text_color))
        }
    }
}
