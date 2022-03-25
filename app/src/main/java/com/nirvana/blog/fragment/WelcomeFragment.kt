package com.nirvana.blog.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ktx.immersionBar
import com.nirvana.blog.R
import com.nirvana.blog.base.BaseFragment
import com.nirvana.blog.databinding.FragmentWelcomeBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>() {

    // 倒计时用 Flow 来实现
    private lateinit var countDown: Flow<Int>
    // 倒计时多久
    private val count = 3
    // 结束时取消任务
    private lateinit var countDownJob: Job
    // 欢迎页结束过后的回调
    private lateinit var onTimeout: () -> Unit

    companion object {
        @JvmStatic
        fun newInstance(onTimeout: () -> Unit) = WelcomeFragment().apply {
            this.onTimeout = onTimeout
        }
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentWelcomeBinding =
        FragmentWelcomeBinding.inflate(inflater, container, false)

    override fun initView() {
        // 初始化倒计时时间的显示
        binding.timeout = count.toString()
    }

    override fun initObserver() {
        // 拦截所有事件，不然会点击到后面 MainActivity 的内容
        binding.welcomeRoot.setOnClickListener {  }
        // 跳过按钮
        binding.jumpOver.setOnClickListener {
            finish()
        }
        // 设置倒计时
        countDown = flow {
            for (i in count downTo 0) {
                emit(i)
                delay(1000)
            }
        }
        countDownJob = lifecycleScope.launchWhenResumed {
            countDown.collect {
                binding.timeout = it.toString()
                if (it == 0) {
                    finish()
                }
            }
        }
    }

    override fun initStatusBar() {
        // 设置状态栏消失
        immersionBar {
            hideBar(BarHide.FLAG_HIDE_STATUS_BAR)
        }
    }

    private fun finish() {
        // 让跳过按钮消失，不然动画的时候会不美观
        binding.jumpOver.visibility = View.GONE
        // 若还在倒计时就可以取消
        countDownJob.cancel()
        // 移除自己，设置移除的动画，向左划走
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_rtl_fast, R.anim.slide_out_rtl_fast)
            .remove(this)
            .commitAllowingStateLoss()
        onTimeout()
    }

}