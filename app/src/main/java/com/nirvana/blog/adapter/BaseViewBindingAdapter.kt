package com.nirvana.blog.adapter

import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * ViewBinding 与 BaseQuickAdapter 结合
 * 统一使用 BaseViewBindingViewHolder 替代 BaseViewHolder
 * 在子类中只需要重写 bind 方法返回对应的 BaseViewBindingViewHolder 即可
 */
abstract class BaseViewBindingAdapter<T, VB : ViewBinding, VH : BaseViewBindingViewHolder<VB>>(
    data: MutableList<T>? = null
) : BaseQuickAdapter<T, BaseViewBindingViewHolder<VB>>(0, data) {

    /**
     * 该方法是 BaseQuickAdapter 的方法，默认实现中会利用构造器中传入的 layoutResId 创建对应的 View
     * 但是我们不需要，我们是通过 ViewBinding 创建，因此只需要交给子类做就好
     * 只需要返回 ViewHolder 即可
     */
    override fun onCreateDefViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewBindingViewHolder<VB> = bind(parent)

    abstract fun bind(parent: ViewGroup): BaseViewBindingViewHolder<VB>

}

/**
 * 带有 ViewBinding 实例的 ViewHolder
 * 有需求可以自定义 ViewHolder 的，可以继承 BaseViewBindingViewHolder，并且配合上面的 BaseViewBindingAdapter
 */
open class BaseViewBindingViewHolder<VB : ViewBinding>(
    val binding: VB
) : BaseViewHolder(binding.root)
