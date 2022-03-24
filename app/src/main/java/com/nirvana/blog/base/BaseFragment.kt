package com.nirvana.blog.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    private var isFirstLoad = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (_binding == null) {
            _binding = bind(inflater, container)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initStatusBar()
        initView()
        initObserver()
    }

    abstract fun bind(inflater: LayoutInflater, container: ViewGroup?): VB

    protected open fun initView() {}
    protected open fun initObserver() {}
    protected open fun initStatusBar() {}

    /**
     * 懒加载实现 initXXXLazy 方法，在 onResume 时再请求数据
     */
    protected open fun initViewLazy() {}
    protected open fun initObserverLazy() {}

    /**
     * 回退处理事件，Fragment 本身是没有的
     */
    open fun onBackPressed(): Boolean = false

    /**
     * 在Fragment中使用View Binding需要多加注意，
     * 如果使用不当它会引发内存泄漏，如果你没有在onDestroy中将view置空，那么它就不会从内存中清除
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        /*
         * 懒加载的请求只能发送一次，防止重复加载
         */
        if (isFirstLoad) {
            isFirstLoad = false
            initViewLazy()
            initObserverLazy()
        }
    }

}