package com.incursio.newster.logging

import android.annotation.SuppressLint
import android.util.Log
import timber.log.Timber

class ReleaseTree : Timber.Tree() {
    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        when (priority) {
            Log.WARN -> {
                Log.w(tag, message, t)
            }
            Log.ERROR -> {
                Log.e(tag, message, t)
            }
            else -> {
            }
        }
    }

}
