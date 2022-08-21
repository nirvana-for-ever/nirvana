package com.nirvana.blog.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.nirvana.blog.R
import com.nirvana.blog.utils.ScreenUtils
import kotlinx.android.synthetic.main.dialog_new_apk_update.*

class OpenAuthorityDialog : Dialog {

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

    fun showOpenAuthority(){
        show()
        tv_update_new_content.text = context.resources.getString(R.string.apk_install_permission)
        tv_update_new_confirm.text = context.resources.getString(R.string.open_now)
        tv_update_new_cancel.text = context.resources.getString(R.string.cancel)
    }

    private fun initEvent(){
        tv_update_new_confirm.setOnClickListener {
            mOnOpenAuthorityConfirmListener?.onConfirmDownload()
            dismiss()
        }
        tv_update_new_cancel.setOnClickListener {
            dismiss()
        }
        iv_update_new_close.setOnClickListener {
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