package com.nirvana.blog.fragment.message

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nirvana.blog.adapter.NoMoreFooterAdapter
import com.nirvana.blog.adapter.message.InteractionPagingAdapter
import com.nirvana.blog.base.BaseFragment
import com.nirvana.blog.databinding.FragmentInteractionBinding
import com.nirvana.blog.utils.Constants
import com.nirvana.blog.utils.DensityUtils
import com.nirvana.blog.utils.rootActivity
import com.nirvana.blog.viewmodel.message.MessageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class InteractionFragment : BaseFragment<FragmentInteractionBinding>() {

    companion object {
        @JvmStatic
        fun newInstance(type: Int): InteractionFragment {
            return InteractionFragment().apply {
                arguments = Bundle().apply {
                    putInt("type", type)
                }
            }
        }
    }

    private val viewModel: MessageViewModel by viewModels(ownerProducer = { rootActivity!! })

    private val pagingAdapter = InteractionPagingAdapter()

    private var type: Int = 0

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentInteractionBinding.inflate(inflater, container, false)

    override fun initView() {
        type = requireArguments().getInt("type")

        binding.messageInteractionRv.apply {
            adapter = pagingAdapter.withLoadStateFooter(NoMoreFooterAdapter())
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    super.getItemOffsets(outRect, view, parent, state)
                    if (parent.getChildAdapterPosition(view) != (pagingAdapter.itemCount)) {
                        outRect[0, 0, 0] = DensityUtils.dip2px(requireContext(), 10f)
                    }
                }
            })
        }
    }

    override fun initObserver() {
        // TODO 订阅信息 待开发
        if (type != Constants.MESSAGE_INTERACTION_SUBSCRIBE_TYPE) {
            lifecycleScope.launch {
                viewModel.messageFlow(type).collectLatest {
                    pagingAdapter.submitData(it)
                }
            }
        }

    }

}