package com.nirvana.blog.adapter.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nirvana.blog.adapter.BaseViewBindingAdapter
import com.nirvana.blog.adapter.BaseViewBindingViewHolder
import com.nirvana.blog.databinding.CarouselViewItemBinding

class CarouselRecyclerViewAdapter(private val imgIds: MutableList<Int>) :
    RecyclerView.Adapter<BaseViewBindingViewHolder<CarouselViewItemBinding>>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewBindingViewHolder<CarouselViewItemBinding> {
        return BaseViewBindingViewHolder(
            CarouselViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: BaseViewBindingViewHolder<CarouselViewItemBinding>,
        position: Int
    ) {
        holder.binding.carouselImg.setImageResource(imgIds[position % imgIds.size])
    }

    override fun getItemCount(): Int = Int.MAX_VALUE
}