package com.nirvana.blog.activity.tag

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.nirvana.blog.R
import com.nirvana.blog.adapter.BaseViewBindingViewHolder
import com.nirvana.blog.adapter.tag.IndexTagSettingChosenRecyclerViewAdapter
import com.nirvana.blog.adapter.tag.IndexTagSettingUnchosenRecyclerViewAdapter
import com.nirvana.blog.base.BaseActivity
import com.nirvana.blog.databinding.ActivityIndexTagSettingBinding
import com.nirvana.blog.databinding.IndexTagSettingItemBinding
import com.nirvana.blog.entity.ui.tag.Tag
import com.nirvana.blog.utils.Constants
import com.nirvana.blog.utils.StatusBarUtils.setBaseStatusBar
import com.nirvana.blog.utils.rootActivity
import com.nirvana.blog.viewmodel.tag.TagViewModel

class IndexTagSettingActivity : BaseActivity<ActivityIndexTagSettingBinding>() {

    private val viewModel: TagViewModel by lazy { ViewModelProvider(rootActivity!!)[TagViewModel::class.java] }

    private val chosenTags = mutableListOf(
        Tag("-1", "综合", "", 0L),
        Tag("-2", "最新", "", 0L)
    )

    private val unchosenTags = mutableListOf<Tag>()

    private var editStatus = false

    private val itemTouchHelper = ItemTouchHelper(IndexTagSettingChosenItemTouchHelperCallback())

    private val chosenTagsAdapter: IndexTagSettingChosenRecyclerViewAdapter =
        IndexTagSettingChosenRecyclerViewAdapter(
            chosenTags,
            {
                chosenTags.remove(it)
                unchosenTags.add(it)
                unchosenTagsAdapter.notifyItemInserted(unchosenTags.size - 1)
            },
            {
                itemTouchHelper.startDrag(it)
            }
        )

    private val unchosenTagsAdapter: IndexTagSettingUnchosenRecyclerViewAdapter =
        IndexTagSettingUnchosenRecyclerViewAdapter(unchosenTags) {
            unchosenTags.remove(it)
            chosenTags.add(it)
            chosenTagsAdapter.notifyItemInserted(chosenTags.size - 1)
        }

    override fun bind(): ActivityIndexTagSettingBinding =
        ActivityIndexTagSettingBinding.inflate(layoutInflater)

    override fun initStatusBar() {
        setBaseStatusBar {
            titleBar(binding.indexTagToolbar)
        }
    }

    override fun initView() {
        // 获取全部的标签
        viewModel.getAllTags()
        // 初始化已选择标签的 rv
        viewModel.userTags.value?.let { chosenTags.addAll(it) }
        binding.indexTagChosenRv.apply {
            adapter = chosenTagsAdapter
            layoutManager = FlexboxLayoutManager(context)
        }
        // 拖拽帮助器绑定 rv
        itemTouchHelper.attachToRecyclerView(binding.indexTagChosenRv)
    }

    override fun initListener() {
        // 监听全部标签的查询返回
        viewModel.allTags.observe(this) {
            unchosenTags.addAll(it.filter { tag ->
                !chosenTags.contains(tag)
            })
            // 未订阅标签的 rv
            binding.indexTagUnchosenRv.apply {
                adapter = unchosenTagsAdapter
                layoutManager = FlexboxLayoutManager(context)
            }
        }

        // 进入或退出编辑状态
        binding.indexTagSettingEditBtn.setOnClickListener {
            if (!editStatus) {
                editStatus = true
                binding.indexTagSettingEditBtn.text =
                    resources.getString(R.string.index_tag_setting_finish_btn_string)
            } else {
                editStatus = false
                binding.indexTagSettingEditBtn.text =
                    resources.getString(R.string.index_tag_setting_edit_btn_string)
            }
            chosenTagsAdapter.editStatusChange(editStatus)
        }

        // 退出编辑
        binding.back.setOnClickListener {
            // 保存标签的改动
            saveTagChanging()
        }
    }

    override fun onBackPressed() {
        // 保存标签的改动
        saveTagChanging()
    }

    /**
     * 保存标签的改动
     */
    private fun saveTagChanging() {
        // 保存改动，首先判断有没有改动
        val preTags = viewModel.userTags.value!!
        if (chosenTags.size - 2 != preTags.size) {
            // 个数发生变化，肯定有变动，直接保存改动
            viewModel.saveTagChanging(chosenTags.subList(2, chosenTags.size)) {
                super.onBackPressed()
            }
            return setResult(Constants.INDEX_TAG_SETTING_CHANGED)
        } else {
            for (i in 2 until chosenTags.size) {
                if (chosenTags[i].tagId != preTags[i - 2].tagId) {
                    // 有一个不一样，说明标签变化了或者顺序调整了，保存改动
                    viewModel.saveTagChanging(chosenTags.subList(2, chosenTags.size)) {
                        super.onBackPressed()
                    }
                    return setResult(Constants.INDEX_TAG_SETTING_CHANGED)
                }
            }
        }
        setResult(Constants.INDEX_TAG_SETTING_UNCHANGED)
        super.onBackPressed()
    }

    inner class IndexTagSettingChosenItemTouchHelperCallback : ItemTouchHelper.Callback() {

        /**
         * 返回值 int 根据位移包含很多数据，拖拽只需要设置 dragFlags，不用设置 swipe
         */
        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            // 全方位都可以拖动
            val dragFlags =
                ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        /**
         * @param viewHolder 当前被拖拽的 vh
         * @param target 拖拽到的地方的 vh
         */
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val targetTag =
                (target as BaseViewBindingViewHolder<IndexTagSettingItemBinding>).binding.tag!!
            return if (!targetTag.tagId.startsWith("-")) {
                // 得到当拖拽的viewHolder的Position
                val fromPosition = viewHolder.bindingAdapterPosition
                // 拿到当前拖拽到的item的viewHolder
                val toPosition = target.bindingAdapterPosition
                val curTag =
                    (viewHolder as BaseViewBindingViewHolder<IndexTagSettingItemBinding>).binding.tag!!
                chosenTagsAdapter.tags.remove(curTag)
                chosenTagsAdapter.tags.add(chosenTagsAdapter.tags.indexOf(targetTag), curTag)
                chosenTagsAdapter.notifyItemMoved(fromPosition, toPosition)
                true
            } else {
                false
            }
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

        override fun isLongPressDragEnabled(): Boolean {
            return false
        }
    }

}