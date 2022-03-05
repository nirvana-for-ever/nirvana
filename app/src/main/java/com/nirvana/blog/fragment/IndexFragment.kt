package com.nirvana.blog.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.nirvana.blog.adapter.BaseFragmentViewPagerAdapter
import com.nirvana.blog.adapter.tag.IndexTagsButtonChangeHelper
import com.nirvana.blog.adapter.tag.IndexTagsRecyclerViewAdapter
import com.nirvana.blog.base.BaseFragment
import com.nirvana.blog.databinding.FragmentIndexBinding
import com.nirvana.blog.entity.Tag
import com.nirvana.blog.fragment.article.ArticleInfoFragment
import com.nirvana.blog.utils.StatusBarUtils.setBaseStatusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndexFragment : BaseFragment<FragmentIndexBinding>() {

    companion object {
        @JvmStatic
        fun newInstance() = IndexFragment()
    }

    // TODO 根据用户信息获取具体的标签
    private val tags = mutableListOf(
        Tag("-1", "综合", "", 0L),
        Tag("-2", "最新", "", 0L),
        Tag("3", "Android", "", 0L),
        Tag("4", "Spring", "", 0L),
        Tag("5", "SpringBoot", "", 0L),
        Tag("6", "Mybatis", "", 0L),
        Tag("7", "RecyclerView", "", 0L),
        Tag("8", "最新", "", 0L),
        Tag("9", "最新", "", 0L),
        Tag("10", "最新", "", 0L),
        Tag("11", "最新", "", 0L),
        Tag("12", "最新", "", 0L),
        Tag("13", "最新", "", 0L),
    )
    // TODO 根据具体返回的标签顺序设置
    private val tagsOrder = mapOf(
        "-1" to 0,
        "-2" to 1,
        "3" to 2,
        "4" to 3,
        "5" to 4,
        "6" to 5,
        "7" to 6,
        "8" to 7,
        "9" to 8,
        "10" to 9,
        "11" to 10,
        "12" to 11,
        "13" to 12,
    )

    private val tagsAdapter by lazy {
        IndexTagsRecyclerViewAdapter(
            tags,
            IndexTagsButtonChangeHelper(
                curTagId = "-1",
                onTagsBtnChange = ::changeTag
            )
        )
    }
    private val tagFragments by lazy {
        // TODO 根据用户的具体标签创建对应个数的 fragment
        tags.map {
            { ArticleInfoFragment.newInstance(it.tagId) }
        }
    }



    override fun bind(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentIndexBinding.inflate(inflater, container, false)

    override fun initView() {
        // 设置首页上方标签
        binding.indexTagsRv.apply {
            adapter = tagsAdapter
            layoutManager = LinearLayoutManager(this@IndexFragment.context, LinearLayoutManager.HORIZONTAL, false)
        }
        // 设置文章信息的 vp
        binding.indexArticleVp.apply {
            // 该方法用于取消滑动到边缘的阴影
            getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
            // viewPager2 搭配 fragment 专属适配器
            adapter = BaseFragmentViewPagerAdapter(
                childFragmentManager,
                lifecycle,
                tagFragments
            )
            /*
             * 预加载和缓存所有页，但是 fragment 都设置成懒加载，在onResume中再请求数据，并且只请求一次，除非刷新
             */
            offscreenPageLimit = tagFragments.size
        }
    }

    override fun initObserver() {
        // TODO 标签设置按钮点击事件
        binding.indexTagsSetting.apply {
            setOnClickListener {
                println("====================")
            }
        }
    }

    /**
     * 首页标签改变事件，需要去获取对应标签的博客内容，切换 vp 的页码
     */
    private fun changeTag(id: String) {
        binding.indexArticleVp.setCurrentItem(tagsOrder[id]!!, false)
    }

}