package com.nirvana.blog.activity

import android.animation.ArgbEvaluator
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.gyf.immersionbar.BarHide
import com.nirvana.blog.R
import com.nirvana.blog.adapter.BaseFragmentViewPagerAdapter
import com.nirvana.blog.base.BaseActivity
import com.nirvana.blog.databinding.ActivityMainBinding
import com.nirvana.blog.fragment.*
import com.nirvana.blog.utils.Constants
import com.nirvana.blog.utils.StatusBarUtils.setBaseStatusBar
import com.nirvana.blog.utils.rootActivity
import com.nirvana.blog.utils.setNormalSensitivity
import com.nirvana.blog.utils.toastShort
import com.nirvana.blog.viewmodel.user.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var onPageChangeCallback: MainOnPageChangeCallback

    private var lastBackTimeMillis = 0L

    private val indexFragment = IndexFragment.newInstance()
    private val communityFragment = CommunityFragment.newInstance()
    private val subscriptionFragment = SubscriptionFragment.newInstance()
    private val messageFragment = MessageFragment.newInstance()
    private val meFragment = MeFragment.newInstance()

    private val accountViewModel: AccountViewModel by viewModels()

    init {
        rootActivity = this
    }

    override fun bind(): ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

    override fun initView() {
        // 获取用户登录信息
        accountViewModel.info(Constants.GET_USER_INFO_SIMPLE)
        // 展示欢迎页
        showWelcome()
    }

    override fun initListener() {
        accountViewModel.simpleUserInfo.observe(this) {
            // vp 设置
            binding.apply {
                // 配置 viewPager2
                mainViewPager.apply {
                    // 该方法用于取消滑动到边缘的阴影
                    getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
                    // 缓存多少页，全部都缓存
                    offscreenPageLimit = 5

                    // viewPager2 搭配 fragment 专属适配器
                    adapter = BaseFragmentViewPagerAdapter(
                        supportFragmentManager,
                        lifecycle,
                        listOf(
                            { indexFragment },
                            { communityFragment },
                            { subscriptionFragment },
                            { messageFragment },
                            { meFragment },
                        )
                    )

                    // 获取底部栏的 View 对象
                    val imgs = listOf(
                        indexImgView,
                        communityImgView,
                        subscriptionImgView,
                        messageImgView,
                        meImgView
                    )
                    val selectedImgs = listOf(
                        indexFullImgView,
                        communityFullImgView,
                        subscriptionFullImgView,
                        messageFullImgView,
                        meFullImgView
                    )
                    val texts = listOf(
                        indexTextView,
                        communityTextView,
                        subscriptionTextView,
                        messageTextView,
                        meTextView
                    )

                    // 添加 page 滑动的监听器回调，添加滑动的渐变动画
                    onPageChangeCallback = MainOnPageChangeCallback(imgs, selectedImgs, texts)
                    registerOnPageChangeCallback(onPageChangeCallback)

                    // 设置 vp 灵敏度
                    setNormalSensitivity()
                }
            }
            binding.apply {
                indexCs.setOnClickListener { mainViewPager.setCurrentItem(0, false) }
                communityCs.setOnClickListener { mainViewPager.setCurrentItem(1, false) }
                subscriptionCs.setOnClickListener { mainViewPager.setCurrentItem(2, false) }
                messageCs.setOnClickListener { mainViewPager.setCurrentItem(3, false) }
                meCs.setOnClickListener { mainViewPager.setCurrentItem(4, false) }
            }

            // 观察一次就行，用于第一次用户信息的监听，需要移除，否则之后一旦用户数据更新就会全部重新加载一遍，那就完犊子
            accountViewModel.simpleUserInfo.removeObservers(this)
        }
    }

    /**
     * 回退事件处理，根据各自 Fragment 的需求处理回退事件
     */
    override fun onBackPressed() {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastBackTimeMillis > 2500L) {
            lastBackTimeMillis = currentTimeMillis
            toastShort("再按一次退出程序")
        } else {
            super.onBackPressed()
        }
    }

    /**
     * 清理掉不需要的东西
     */
    override fun onDestroy() {
        if (::onPageChangeCallback.isInitialized) {
            binding.mainViewPager.unregisterOnPageChangeCallback(onPageChangeCallback)
        }
        super.onDestroy()
    }

    inner class MainOnPageChangeCallback(
        private val imgs: List<ImageView>,
        private val selectedImgs: List<ImageView>,
        private val texts: List<TextView>
    ) : ViewPager2.OnPageChangeCallback() {

        private val maxPos: Int = imgs.size - 1
        private val argbEvaluator = ArgbEvaluator()

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            imgs[position].alpha = positionOffset
            selectedImgs[position].alpha = 1 - positionOffset
            texts[position]
                .setTextColor(
                    argbEvaluator.evaluate(
                        positionOffset,
                        ContextCompat.getColor(this@MainActivity, R.color.primary),
                        ContextCompat.getColor(this@MainActivity, R.color.black)
                    ) as Int
                )
            if (position < maxPos) {
                imgs[position + 1].alpha = 1 - positionOffset
                selectedImgs[position + 1].alpha = positionOffset
                texts[position + 1]
                    .setTextColor(
                        argbEvaluator.evaluate(
                            positionOffset,
                            ContextCompat.getColor(this@MainActivity, R.color.black),
                            ContextCompat.getColor(this@MainActivity, R.color.primary)
                        ) as Int
                    )
            }
        }

        override fun onPageSelected(position: Int) {
            for (i in imgs.indices) {
                if (i != position) {
                    imgs[i].alpha = 1f
                    selectedImgs[i].alpha = 0f
                    texts[i].setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))
                } else {
                    imgs[i].alpha = 0f
                    selectedImgs[i].alpha = 1f
                    texts[i].setTextColor(
                        ContextCompat.getColor(
                            this@MainActivity,
                            R.color.primary
                        )
                    )
                }
            }
        }
    }

    /**
     * 展示欢迎页
     */
    private fun showWelcome() {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_root, WelcomeFragment.newInstance {
                // 欢迎页过后重新将状态栏显示
                setBaseStatusBar {
                    hideBar(BarHide.FLAG_SHOW_BAR)
                }
            })
            .commitAllowingStateLoss()
    }
}