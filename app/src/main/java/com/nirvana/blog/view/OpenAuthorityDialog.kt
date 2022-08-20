package com.nirvana.blog.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.nirvana.blog.R
import kotlinx.android.synthetic.main.dialog_apk_update.*

class OpenAuthorityDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_apk_update)

        initWindow()
        initEvent()
    }

    private fun initWindow(){
        val lp = window?.attributes
//        lp?.width = (ScreenUtils.getScreenWidth(context)*0.5).toInt()
        window?.attributes = lp
    }

    fun showOpenAuthority(){
        show()
        tv_update_title.text = context.resources.getString(R.string.notice)
        tv_update_content.text = context.resources.getString(R.string.apk_install_permission)
        tv_update_update.text = context.resources.getString(R.string.open_now)
        tv_update_close.text = context.resources.getString(R.string.cancel)
    }

    private fun initEvent(){
        tv_update_update.setOnClickListener {
            mOnOpenAuthorityConfirmListener?.onConfirmDownload()
            dismiss()
        }
        tv_update_close.setOnClickListener {
            dismiss()
        }
    }

    interface OnOpenAuthorityConfirmListener{
        fun onConfirmDownload()
    }

    private var mOnOpenAuthorityConfirmListener:OnOpenAuthorityConfirmListener? = null

    fun setOnApkDownloadConfirmListener(mOnOpenAuthorityConfirmListener:OnOpenAuthorityConfirmListener){
        this.mOnOpenAuthorityConfirmListener = mOnOpenAuthorityConfirmListener
    }

}