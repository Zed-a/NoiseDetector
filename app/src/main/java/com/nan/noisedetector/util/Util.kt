package com.nan.noisedetector.util

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by nan on 2019-12-19.
 */
fun getTime(): String = SimpleDateFormat("HH:mm:ss", Locale.SIMPLIFIED_CHINESE).format(Date())
fun getDate(): String = SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE).format(Date())
fun logD(tag: String, message: String) = Log.d(tag, message)
fun isEmpty(str: String?): Boolean = str==null || str.isEmpty()

fun showSoftKeyboard(context: Context?, editText: EditText) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        editText.requestFocusFromTouch()
    }
    val timer = Timer()
    timer.schedule(object : TimerTask() {
        override fun run() {
            var i = 0
            while (editText.windowToken == null) {
                if (i == 10) {
                    break
                }
                try {
                    Thread.sleep(300)
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
                i++
            }
            val imm: InputMethodManager? = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        }
    }, 300)
}

fun dismissSoftKeyboard(activity: Activity?, text: EditText) {
    (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(text.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}