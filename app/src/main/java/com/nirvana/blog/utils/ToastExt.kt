package com.nirvana.blog.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

private var toast: Toast? = null

fun Fragment.toastShort(text: String) {
    show(generateToast(requireContext(), text, Toast.LENGTH_SHORT))
}
fun Fragment.toastShort(resId: Int) {
    show(generateToast(requireContext(), resId, Toast.LENGTH_SHORT))
}
fun Fragment.toastLong(text: String) {
    show(generateToast(requireContext(), text, Toast.LENGTH_LONG))
}
fun Fragment.toastLong(resId: Int) {
    show(generateToast(requireContext(), resId, Toast.LENGTH_LONG))
}

fun Activity.toastShort(text: String) {
    show(generateToast(this, text, Toast.LENGTH_SHORT))
}
fun Activity.toastShort(resId: Int) {
    show(generateToast(this, resId, Toast.LENGTH_SHORT))
}
fun Activity.toastLong(text: String) {
    show(generateToast(this, text, Toast.LENGTH_LONG))
}
fun Activity.toastLong(resId: Int) {
    show(generateToast(this, resId, Toast.LENGTH_LONG))
}

private fun generateToast(context: Context, text: String, type: Int) =
    Toast.makeText(context, text, type)
private fun generateToast(context: Context, resId: Int, type: Int) =
    Toast.makeText(context, resId, type)

private fun show(newToast: Toast) {
    if (toast != null) {
        toast?.cancel()
    }
    toast = newToast
    toast?.show()
}
