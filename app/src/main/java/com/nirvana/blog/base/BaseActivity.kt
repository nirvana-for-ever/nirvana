package com.nirvana.blog.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.nirvana.blog.utils.StatusBarUtils.setBaseStatusBar


abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bind()
        setContentView(binding.root)

        initStatusBar()
        initView()
        initListener()
    }

    abstract fun bind(): VB

    protected open fun initView(){}

    protected open fun initListener() {}

    protected open fun initStatusBar() {}
}