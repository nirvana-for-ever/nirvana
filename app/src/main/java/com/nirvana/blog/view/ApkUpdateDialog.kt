package com.nirvana.blog.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.nirvana.blog.R
import com.nirvana.blog.bean.AppConfigBean
import com.nirvana.blog.utils.ScreenUtils
import kotlinx.android.synthetic.main.dialog_apk_update.*
import java.lang.StringBuilder

class ApkUpdateDialog : Dialog {

    private lateinit var info : AppConfigBean

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

    fun showUpdate(info :AppConfigBean){
        show()
        this.info = info
        val sb = StringBuilder()
        for(i in info.message.indices){
            if(i<info.message.size-1){
                sb.append((i+1).toString()+"："+info.message[i]+"\n")
            }else{
                sb.append((i+1).toString()+"："+info.message[i])
            }
        }
        tv_update_content.text = sb
        tv_update_close.visibility = if(info.isForce!="1") View.VISIBLE else View.GONE
        setCanceledOnTouchOutside(info.isForce!="1")
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
        fun onConfirmDownload(info :AppConfigBean)
    }

    private var mOnApkDownloadConfirmListener:OnApkDownloadConfirmListener? = null

    fun setOnApkDownloadConfirmListener(mOnApkDownloadConfirmListener:OnApkDownloadConfirmListener){
        this.mOnApkDownloadConfirmListener = mOnApkDownloadConfirmListener
    }

}