package com.nirvana.blog.utils

import android.app.AlertDialog
import android.content.DialogInterface
import android.widget.Button
import android.widget.TextView

/**
 * 获取 AlertDialog 的控件
 */
fun AlertDialog.getPosBtnColor(): Button {
    return getButton(DialogInterface.BUTTON_POSITIVE)
}

fun AlertDialog.getNegBtnColor(): Button {
    return getButton(DialogInterface.BUTTON_NEGATIVE)
}

fun AlertDialog.getNeuBtnColor(): Button {
    return getButton(DialogInterface.BUTTON_NEUTRAL)
}

fun AlertDialog.getMessageTextView(): TextView {
    return window!!.findViewById(android.R.id.message)
}
