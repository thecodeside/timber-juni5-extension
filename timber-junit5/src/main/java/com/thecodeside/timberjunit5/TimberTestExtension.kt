package com.thecodeside.timberjunit5

import android.util.Log
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.Extension
import org.junit.jupiter.api.extension.ExtensionContext
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * A JUnit [Extension] that plants a [Timber.Tree] before a test is executed,
 * and uproots the [Timber.Tree] afterwards.
 *
 *
 * This is useful as a planted [Timber.Tree] would exist between test classes, which may be
 * undesirable.
 *
 * @author TheCodeSide Artur Latoszewski (https://www.thecodeside.com/)
 */
class TimberTestExtension constructor(private val rules: Rules = Rules()) : BeforeEachCallback,
    AfterEachCallback {

    private lateinit var mTree: BufferedJUnitTimberTree

    override fun beforeEach(context: ExtensionContext?) {
        mTree = BufferedJUnitTimberTree(rules)
        Timber.plant(mTree)
    }

    override fun afterEach(context: ExtensionContext?) {
        if (context?.executionException?.isPresent == true) {
            mTree.flushLogs()
        }
        Timber.uproot(mTree)
    }

    /**
     * Defines a set of rules in which the [TimberTestExtension]'s internal Timber tree must
     * adhere to when intercepting log messages.
     *
     *
     * The types of rules applied are:
     *
     *  1. Min priority - What is the lowest level of log type that should be logged.
     *  1. Show thread - Whether the Thread ID and name should be logged.
     *  1. Show timestamp - Whether the current time should be logged.
     *
     */
    data class Rules(
        /**
         * Defines what the lowest level of log type that should be logged.
         *
         *
         * This can be:
         *
         *  1. [Log.VERBOSE]
         *  1. [Log.DEBUG]
         *  1. [Log.INFO]
         *  1. [Log.WARN]
         *  1. [Log.ERROR]
         *
         *
         * @property minPriority the Android log priority.
         */
        val minPriority: Int = Log.VERBOSE,
        /**
         * Defines whether the Thread ID and name should be logged
         *
         * @property showThread whether the thread details are shown.
         */
        val showThread: Boolean = false,
        /**
         * Defines whether the timestamp should be logged
         *
         * @property showTimestamp whether the timestamp is shown.
         */
        val showTimestamp: Boolean = true,
        /**
         * Defines whether the logs are only output if the unit test fails.
         *
         * @property logOnlyWhenTestFails whether the logs are only output when a test fails.
         */
        val logOnlyWhenTestFails: Boolean = true
    )

    /**
     * A Timber tree that logs to the System.out rather than using the Android logger.
     */
    private class BufferedJUnitTimberTree constructor(private val rules: Rules) : DebugTree() {
        private val logMessageBuffer: MutableList<String> = ArrayList()
        private val bufferLock = Any()

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            val logMessage = createLogMessage(rules, priority, tag, message)
                ?: return
            if (rules.logOnlyWhenTestFails) {
                synchronized(bufferLock) { logMessageBuffer.add(logMessage) }
            } else {
                println(logMessage)
            }
        }

        /**
         * Flushes all the previously stored log messages.
         */
        fun flushLogs() {
            synchronized(bufferLock) {
                val iterator = logMessageBuffer.iterator()
                while (iterator.hasNext()) {
                    println(iterator.next())
                    iterator.remove()
                }
            }
        }
    }
}

/**
 * Creates a log message based on the rules and Timber log details.
 *
 * @param rules    the rules used to construct the message.
 * @param priority the priority of the log.
 * @param tag      the tag of the log.
 * @param message  the message of the log.
 * @return a log message (may be null).
 */
private fun createLogMessage(
    rules: TimberTestExtension.Rules,
    priority: Int,
    tag: String?,
    message: String
): String? {
    // Avoid logging if the priority is too low.
    if (priority < rules.minPriority) {
        return null
    }

    // Obtain the correct log type prefix.
    val type: Char = when (priority) {
        Log.VERBOSE -> 'V'
        Log.DEBUG -> 'D'
        Log.INFO -> 'I'
        Log.WARN -> 'W'
        Log.ERROR -> 'E'
        else -> 'E'
    }
    val logBuilder = StringBuilder()
    if (rules.showTimestamp) {
        logBuilder
            .append(THREAD_LOCAL_FORMAT.get().format(System.currentTimeMillis()))
            .append(" ")
    }
    if (rules.showThread) {
        val thread = Thread.currentThread()
        logBuilder
            .append(thread.id)
            .append("/")
            .append(thread.name)
            .append(" ")
    }
    logBuilder
        .append(type)
        .append("/")
        .append(tag)
        .append(": ")
        .append(message)
    return logBuilder.toString()
}

/**
 * A thread local is used as the [DateFormat] class is not thread-safe.
 */
private val THREAD_LOCAL_FORMAT: ThreadLocal<DateFormat> = object : ThreadLocal<DateFormat>() {
    override fun initialValue(): DateFormat {
        return SimpleDateFormat("HH:mm:ss:SSSSSSS", Locale.ENGLISH)
    }
}