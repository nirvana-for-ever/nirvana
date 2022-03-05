package com.nirvana.blog.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

open class BaseFragmentViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    /**
     * 最好用懒创建 Fragment 的方式，更能适配所有的情况，对于本项目实际上传递 List<Fragment> 也没什么问题
     * 因为本项目预加载并且缓存所有的页，所以一开始就需要创建全部的 Fragment
     * 对于其他情况，如果不需要预加载所有的页，就最好传递 List<() -> Fragment>，需要时再创建 Fragment
     */
    private val fragments: List<() -> Fragment>
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position].invoke()

}