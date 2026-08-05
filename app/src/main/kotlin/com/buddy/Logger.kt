package com.buddy

import android.util.Log

object Logger {
    private const val TAG = "BuddyCore"
    fun i(msg: String) = Log.i(TAG, msg)
    fun e(msg: String, t: Throwable? = null) = Log.e(TAG, msg, t)
    fun d(msg: String) = Log.d(TAG, msg)
}
