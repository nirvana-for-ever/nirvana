package com.nirvana.blog.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContract
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.nirvana.blog.activity.tag.IndexTagSettingActivity
import com.nirvana.blog.adapter.BaseFragmentViewPagerAdapter
import com.nirvana.blog.adapter.BaseViewBindingViewHolder
import com.nirvana.blog.adapter.tag.IndexTagsButtonChangeHelper
import com.nirvana.blog.adapter.tag.IndexTagsRecyclerViewAdapter
import com.nirvana.blog.base.BaseFragment
import com.nirvana.blog.databinding.FragmentIndexBinding
import com.nirvana.blog.databinding.IndexTagsButtonBinding
import com.nirvana.blog.entity.ui.tag.Tag
import com.nirvana.blog.ext.CenterLayoutManager
import com.nirvana.blog.fragment.article.ArticleInfoFragment
import com.nirvana.blog.utils.Constants
import com.nirvana.blog.utils.DensityUtils
import com.nirvana.blog.utils.isLogin
import com.nirvana.blog.utils.rootActivity
import com.nirvana.blog.viewmodel.tag.TagViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndexFragment : BaseFragment<FragmentIndexBinding>() {

    companion object {
        @JvmStatic
        fun newInstance() = IndexFragment()
    }

    private val tagViewModel: TagViewModel by viewModels(ownerProducer = { rootActivity!! })

    // 标签
    private val tags: MutableList<Tag> = mutableListOf()

    // 标签id-标签顺序
    private val tagsOrder: MutableMap<String, Int> = mutableMapOf()

    // 标签顺序-标签id
    private val tagsPosition: MutableMap<Int, String> = mutableMapOf()

    private var tagsHelper: IndexTagsButtonChangeHelper? = null

    private var tagsAdapter: IndexTagsRecyclerViewAdapter? = null

    private var tagFragments: List<() -> Fragment>? = null

    private var tagsRvScrollListener: IndexTagOnScrollListener? = null

    private var isLoginCache = isLogin

    private val indexTagSettingLauncher =
        registerForActivityResult(object : ActivityResultContract<Intent, Boolean>() {
            override fun createIntent(context: Context, input: Intent) = input
            override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
                return resultCode == Constants.INDEX_TAG_SETTING_CHANGED
            }
        }) {
            if (it) {
                // 刷新 fragment
                initView()
            }
        }

    override fun onResume() {
        super.onResume()
        if (isLoginCache != isLogin) {
            isLoginCache = isLogin
            // 登录状态发生改变，刷新 fragment
            initView()
        }
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentIndexBinding.inflate(inflater, container, false)

    override fun initView() {
        // 初始化成员变量
        initVariable()
        // 获取用户喜欢的标签
        tagViewModel.getUserTags()
    }

    override fun initObserver() {
        // 用户喜欢标签监听事件
        tagViewModel.userTags.observe(this) {
            it.forEachIndexed { index, tag ->
                tags.add(tag)
                tagsOrder[tag.tagId] = index + 2
                tagsPosition[index + 2] = tag.tagId
            }

            tagsHelper = IndexTagsButtonChangeHelper(
                curTagId = "-1",
                onTagsBtnChange = ::changeTag
            )

            tagsAdapter = IndexTagsRecyclerViewAdapter(
                tags,
                tagsHelper!!
            )
            tagFragments = tags.map {
                { ArticleInfoFragment.newInstance(it.tagId) }
            }

            // 设置首页上方标签
            binding.indexTagsRv.apply {
                adapter = tagsAdapter
                layoutManager = CenterLayoutManager(
                    this@IndexFragment.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
                // 让最后一个标签按钮与右侧的设置标签按钮不要重合
                // 注意：刷新时不要重复添加
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        super.getItemOffsets(outRect, view, parent, state)
                        if (parent.getChildAdapterPosition(view) == tagFragments!!.size - 1) {
                            outRect.right = DensityUtils.dip2px(parent.context, 40f)
                        }
                    }
                })
                /*
                 * 添加滑动事件的监听，来改变由于文章详情的 vp 变化让标签也变化
                 * 注意：刷新时不要重复添加
                 */
                addOnScrollListener(tagsRvScrollListener!!)
            }
            // 设置文章信息的 vp
            binding.indexArticleVp.apply {
                // 该方法用于取消滑动到边缘的阴影
                getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
                // viewPager2 搭配 fragment 专属适配器
                adapter = BaseFragmentViewPagerAdapter(
                    childFragmentManager,
                    lifecycle,
                    tagFragments!!
                )
                /*
                 * 预加载和缓存所有页，但是 fragment 都设置成懒加载，在onResume中再请求数据，并且只请求一次，除非刷新
                 */
                offscreenPageLimit = tagFragments!!.size
                /*
                 * 让标签按钮跟着文章详情页的滑动而变化
                 * 需要两步，先让标签的按钮变色，再让标签的 rv 移动
                 */
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        binding.indexTagsRv.apply {
                            try {
                                // 改变标签按钮颜色，如果失败，就通过 catch ，在监听事件中修改（tagsRvScrollListener）
                                val vh =
                                    binding.indexTagsRv.findViewHolderForAdapterPosition(position) as BaseViewBindingViewHolder<IndexTagsButtonBinding>
                                tagsHelper!!.changeCurBtnOutside(
                                    vh.binding.indexTagsBtn,
                                    tagsPosition[position]!!
                                )
                            } catch (e: Exception) {
                                tagsRvScrollListener!!.active = true
                                tagsRvScrollListener!!.position = position
                            }
                            smoothScrollToPosition(position)
                        }
                    }
                })
            }
        }

        binding.indexTagsSetting.setOnClickListener {
            indexTagSettingLauncher.launch(Intent(context, IndexTagSettingActivity::class.java))
        }
    }

    /**
     * 首页标签改变事件，需要去获取对应标签的博客内容，切换 vp 的页码
     */
    private fun changeTag(id: String) {
        binding.indexArticleVp.setCurrentItem(tagsOrder[id]!!, false)
    }

    private fun initVariable() {
        tags.clear()
        tagsOrder.clear()
        tagsPosition.clear()
        tags.add(Tag("-1", "综合", "", 0L))
        tags.add(Tag("-2", "最新", "", 0L))
        tagsOrder["-1"] = 0
        tagsOrder["-2"] = 1
        tagsPosition[0] = "-1"
        tagsPosition[1] = "-2"

        tagsRvScrollListener = IndexTagOnScrollListener()
    }

    inner class IndexTagOnScrollListener : RecyclerView.OnScrollListener() {
        var position: Int? = null
        var active = false

        /**
         * 有三种 state 状态
         * SCROLL_STATE_IDLE 是静止
         * SCROLL_STATE_DRAGGING 是正在拖动
         * SCROLL_STATE_SETTLING 是惯性滑动（动画）
         */
        override fun onScrollStateChanged(
            recyclerView: RecyclerView,
            newState: Int
        ) {
            if (active && newState == RecyclerView.SCROLL_STATE_IDLE) {
                val vh =
                    binding.indexTagsRv.findViewHolderForAdapterPosition(position!!) as BaseViewBindingViewHolder<IndexTagsButtonBinding>
                tagsHelper!!.changeCurBtnOutside(
                    vh.binding.indexTagsBtn,
                    tagsPosition[position]!!
                )
                active = false
            }
        }
    }

}