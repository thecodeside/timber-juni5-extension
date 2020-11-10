package com.thecodeside.timberjunit5

import timber.log.Timber

object LogTester {
    fun log(logType: LogType?, message: String?) {
        when (logType) {
            LogType.VERBOSE -> Timber.v(message)
            LogType.DEBUG -> Timber.d(message)
            LogType.INFO -> Timber.i(message)
            LogType.WARN -> Timber.w(message)
            LogType.ERROR -> Timber.e(message)
        }
    }

    fun log(logType: LogType?, message: String?, throwable: Throwable?) {
        when (logType) {
            LogType.VERBOSE -> Timber.v(throwable, message)
            LogType.DEBUG -> Timber.d(throwable, message)
            LogType.INFO -> Timber.i(throwable, message)
            LogType.WARN -> Timber.w(throwable, message)
            LogType.ERROR -> Timber.e(throwable, message)
        }
    }

    enum class LogType {
        VERBOSE, DEBUG, INFO, WARN, ERROR
    }
}