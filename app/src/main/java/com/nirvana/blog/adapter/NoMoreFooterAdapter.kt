package com.nirvana.blog.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nirvana.blog.R

class NoMoreFooterAdapter :
    LoadStateAdapter<NoMoreFooterAdapter.RecyclerViewFooterFooterViewHolder>() {

    class RecyclerViewFooterFooterViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(
        holder: RecyclerViewFooterFooterViewHolder,
        loadState: LoadState
    ) {

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): RecyclerViewFooterFooterViewHolder {
        return RecyclerViewFooterFooterViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.no_more_footer, parent, false)
        )
    }

    /**
     * loadState is LoadState.NotLoading && loadState.endOfPaginationReached
     * 上述为 true 就展示没有更多数据
     * endOfPaginationReached 代表已经到达底部，就是 PagingSource 中传递的结果
     */
    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.NotLoading && loadState.endOfPaginationReached
    }

}