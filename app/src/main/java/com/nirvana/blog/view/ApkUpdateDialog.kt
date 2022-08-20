package com.nirvana.blog.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.nirvana.blog.R
import com.nirvana.blog.bean.AppConfigBean
import com.nirvana.blog.bean.AppUpdateBean
import com.nirvana.blog.utils.ScreenUtils
import kotlinx.android.synthetic.main.dialog_apk_update.*
import java.lang.StringBuilder

class ApkUpdateDialog : Dialog {

    private lateinit var info : AppUpdateBean

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_apk_update)

        initWindow()
        initEvent()
    }

    private fun initWindow(){
        val lp = window?.attributes
//        lp?.height = (ScreenUtils.getScreenWidth(context)*0.8).toInt()
        window?.attributes = lp
    }

    fun showUpdate(info : AppUpdateBean){
        show()
        this.info = info

        tv_update_content.text = info.data.message
        tv_update_close.visibility = if(!info.data.isForce) View.VISIBLE else View.GONE
        setCanceledOnTouchOutside(!info.data.isForce)
    }

    private fun initEvent(){
        tv_update_update.setOnClickListener {
            mOnApkDownloadConfirmListener?.onConfirmDownload(info)
        }
        tv_update_close.setOnClickListener {
            dismiss()
        }
    }

    interface OnApkDownloadConfirmListener{
        fun onConfirmDownload(info :AppUpdateBean)
    }

    private var mOnApkDownloadConfirmListener:OnApkDownloadConfirmListener? = null

    fun setOnApkDownloadConfirmListener(mOnApkDownloadConfirmListener:OnApkDownloadConfirmListener){
        this.mOnApkDownloadConfirmListener = mOnApkDownloadConfirmListener
    }

}