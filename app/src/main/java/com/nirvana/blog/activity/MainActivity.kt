package com.nirvana.blog.activity

import android.Manifest
import android.animation.ArgbEvaluator
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.viewpager2.widget.ViewPager2
import com.gyf.immersionbar.BarHide
import com.nirvana.blog.R
import com.nirvana.blog.adapter.BaseFragmentViewPagerAdapter
import com.nirvana.blog.base.BaseActivity
import com.nirvana.blog.bean.AppUpdateBean
import com.nirvana.blog.bean.AppUpdateListBean
import com.nirvana.blog.constant.C
import com.nirvana.blog.databinding.ActivityMainBinding
import com.nirvana.blog.fragment.*
import com.nirvana.blog.utils.*
import com.nirvana.blog.utils.StatusBarUtils.setBaseStatusBar
import com.nirvana.blog.view.ApkUpdateDialog
import com.nirvana.blog.view.OpenAuthorityDialog
import com.nirvana.blog.viewmodel.user.AccountViewModel
import com.yanzhenjie.permission.AndPermission
import dagger.hilt.android.AndroidEntryPoint

import java.io.File

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() ,ConfigDownloadUtils.OnConfigDownloadCompleteListener, APKRefreshDownload.OnDownLoadCompleteListener{

    private lateinit var onPageChangeCallback: MainOnPageChangeCallback

    private var lastBackTimeMillis = 0L

    private var isMustUpdate = false
    private var isUpdateComplete = false
    private var isUpdateChecked = false

    private var updateDialog : ApkUpdateDialog? = null
    private var authorityDialog : OpenAuthorityDialog? = null

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
        updateDialog = ApkUpdateDialog(this)
        authorityDialog = OpenAuthorityDialog(this)
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
        updateDialog?.setOnApkDownloadConfirmListener(object :ApkUpdateDialog.OnApkDownloadConfirmListener{
            override fun onConfirmDownload(info: AppUpdateBean) {
                checkUpdatePermission(info.data)
            }
        })
        updateDialog?.setOnDismissListener {
            if(isMustUpdate&&!isUpdateComplete){
                finish()
            }
        }
        authorityDialog?.setOnApkDownloadConfirmListener(object :OpenAuthorityDialog.OnOpenAuthorityConfirmListener{
            override fun onConfirmDownload() {
                val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivityForResult(intent, 1)
            }
        })
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
        updateDialog?.dismiss()
        authorityDialog?.dismiss()
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

    /**
     * 检查更新
     */
    private fun checkUpdate(){
        if (!isUpdateChecked){
            ConfigDownloadUtils.configDownload(C.ANDROID_UPDATE_CONFIG_ADDRESS,this,this)
        }
    }

    override fun onResume() {
        super.onResume()
        checkUpdate()
    }

    override fun onConfigComplete(result: AppUpdateBean?) {
        if(result!=null&&result.data.versionCode.toInt()>0){
            val vn = AppUtils.getVersionName(this)
            val vc = AppUtils.getAppVersionCode(this)
            Log.d("Update", "当前版本-----------》: "+vc)
            Log.d("Update", "当前服务器最新版本-----------》: "+result.data.versionCode)
            if(result.data.versionName==vn&&vc!=0L&&result.data.versionCode.toInt()>vc){
                isMustUpdate = result.data.isForce == true
                isUpdateComplete = false
                updateDialog?.showUpdate(result)
                return
            }
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
                checkPermission()
            }
            isUpdateChecked = true
        }
    }
    @SuppressLint("MissingPermission")
    private fun checkUpdatePermission(apkBean: AppUpdateListBean){
        val updateUrl = apkBean.downloadUrl
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
            .onGranted { permissions ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val haveInstallPermission = packageManager.canRequestPackageInstalls()
                    if(haveInstallPermission){
                        APKRefreshDownload(this).startDownload(updateUrl,this)
                    }else{
                        authorityDialog?.showOpenAuthority()
                    }
                }else{
                    APKRefreshDownload(this).startDownload(updateUrl,this)
                }
            }
            .onDenied { permissions ->
                toastShort(R.string.update_error_1)
            }
            .start()
    }

    @SuppressLint("MissingPermission")
    private fun checkPermission(){
        AndPermission.with(this)
            .runtime()
            .permission(Manifest.permission.READ_PHONE_STATE)
            .onGranted { permissions ->
//                login(0)
            }
            .onDenied { permissions ->

            }
            .start()
    }

    override fun onComplete(isComplete: Boolean) {
        isUpdateComplete = isComplete
        if(isComplete){
            openAPK()
        }else{
            toastShort(R.string.download_fail)
        }
    }

    private fun openAPK() {
        val file = File(APKRefreshDownload.getSaveFilePath())
        val intent = Intent(Intent.ACTION_VIEW)
        val data: Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(this, "com.nirvana.ggxggb.operation.fileprovider", file)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        } else {
            data = Uri.fromFile(file)
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive")
        startActivity(intent)
        finish()
    }
}