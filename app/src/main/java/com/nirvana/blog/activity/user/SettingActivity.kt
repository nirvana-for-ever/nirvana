package com.nirvana.blog.activity.user

import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nirvana.blog.adapter.user.SettingOptionsRecyclerViewAdapter
import com.nirvana.blog.base.BaseActivity
import com.nirvana.blog.databinding.ActivitySettingBinding
import com.nirvana.blog.entity.ui.user.SettingOption
import com.nirvana.blog.entity.ui.user.SubSettingOption
import com.nirvana.blog.utils.StatusBarUtils.setBaseStatusBar
import com.nirvana.blog.utils.isLogin
import com.nirvana.blog.utils.rootActivity
import com.nirvana.blog.utils.toastShort
import com.nirvana.blog.viewmodel.user.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingBinding>() {

    private val options = mutableListOf(
        SettingOption("消息推送", mutableListOf(
            SubSettingOption("文章更新推送"),
            SubSettingOption("新消息推送"),
        )),
        SettingOption("账号设置", mutableListOf(
            SubSettingOption("编辑个人资料"),
            SubSettingOption("账号与安全"),
        )),
        SettingOption("通用设置", mutableListOf(
            SubSettingOption("默认编辑器"),
            SubSettingOption("添加写文章到桌面"),
            SubSettingOption("赞赏设置"),
            SubSettingOption("字号设置"),
            SubSettingOption("隐私设置"),
            SubSettingOption("黑名单设置"),
            SubSettingOption("移动网络下加载图片"),
        )),
        SettingOption("其他", mutableListOf(
            SubSettingOption("回收站"),
            SubSettingOption("清除缓存"),
            SubSettingOption("版本更新"),
            SubSettingOption("分享牛蛙呐", "推荐"),
            SubSettingOption("关于我们"),
        )),
    )

    private val viewModel: AccountViewModel by lazy { ViewModelProvider(rootActivity!!)[AccountViewModel::class.java] }

    override fun bind() = ActivitySettingBinding.inflate(layoutInflater)

    override fun initStatusBar() {
        setBaseStatusBar {
            titleBar(binding.meSettingToolbar)
        }
    }

    override fun initView() {
        binding.meSettingOptionsRv.apply {
            adapter = SettingOptionsRecyclerViewAdapter(options)
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun initListener() {
        binding.meSettingsBack.setOnClickListener {
            finish()
        }

        /*
         * 登出账号
         */
        binding.meSettingsLogout.apply {
            if (isLogin) {
                setOnClickListener {
                    viewModel.logout {
                        /*
                         * 登出完成监听
                         */
                        if (it.success) {
                            setResult(0, Intent().apply { putExtra("isLogout", true) })
                            finish()
                        } else {
                            binding.meSettingsShadow.visibility = View.GONE
                            toastShort(it.msg)
                        }
                    }
                    binding.meSettingsShadow.visibility = View.VISIBLE
                }
            } else {
                visibility = View.GONE
            }
        }

    }
}