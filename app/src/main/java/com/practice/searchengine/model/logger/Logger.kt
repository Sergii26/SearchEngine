package com.practice.searchengine.model.logger

import android.util.Log

class Logger private constructor(private val TAG: String) :
    com.practice.searchengine.model.logger.Log {
    private val priority: Int = Log.DEBUG

    override fun log(message: String?): Logger {
        Log.println(priority, TAG, message!!)
        return this
    }

    override fun withCause(cause: Exception?) {
        Log.println(priority, TAG, Log.getStackTraceString(cause))
    }

    companion object {
        fun withTag(tag: String): Logger {
            return Logger(tag)
        }
    }
}
