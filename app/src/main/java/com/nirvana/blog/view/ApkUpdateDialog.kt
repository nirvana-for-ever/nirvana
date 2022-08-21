package com.nirvana.blog.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Toast
import com.nirvana.blog.R
import com.nirvana.blog.bean.AppUpdateBean
import com.nirvana.blog.utils.ScreenUtils
import kotlinx.android.synthetic.main.dialog_new_apk_update.*

class ApkUpdateDialog : Dialog {

    private lateinit var info : AppUpdateBean

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_new_apk_update)

        initWindow()
        initEvent()
    }

    private fun initWindow(){
        val lp = window?.attributes
        lp?.height = (ScreenUtils.getScreenWidth(context)*1.3).toInt()
        window?.attributes = lp
    }

    fun showUpdate(info : AppUpdateBean){
        show()
        this.info = info

        tv_update_new_version.text = info.data.versionName+".${info.data.versionCode}"
        tv_update_new_content.text = info.data.message
        tv_update_new_content.movementMethod = ScrollingMovementMethod.getInstance()
        iv_update_new_close.visibility = if(!info.data.isForce) View.VISIBLE else View.GONE
        setCanceledOnTouchOutside(!info.data.isForce)
    }

    private fun initEvent(){
        tv_update_new_confirm.setOnClickListener {
            mOnApkDownloadConfirmListener?.onConfirmDownload(info)
        }
        tv_update_new_cancel.setOnClickListener {
            Toast.makeText(context, context.resources.getString(R.string.update_forbiten), Toast.LENGTH_SHORT).show()
        }
        iv_update_new_close.setOnClickListener {
            Toast.makeText(context, context.resources.getString(R.string.update_forbiten_1), Toast.LENGTH_SHORT).show()
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