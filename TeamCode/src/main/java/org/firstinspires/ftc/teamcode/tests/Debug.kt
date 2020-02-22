package org.firstinspires.ftc.teamcode.tests

import android.util.Log

object Debug {
    /**
     * Tag used by logcat
     */
    private const val TAG = "FIRST"
    private const val ERROR_PREPEND = "### ERROR: "
    /**
     * Log a debug message
     * @param message
     */
    fun log(message: String?) {
        Log.i(TAG, message)
    }

    fun log(format: String?, vararg args: Any?) {
        log(String.format(format!!, *args))
    }

    /**
     * Log an error message
     *
     *
     * Messages will be prepended with the ERROR_PREPEND string
     * @param message
     */
    fun error(message: String) {
        Log.e(TAG, ERROR_PREPEND + message)
    }

    fun error(format: String?, vararg args: Any?) {
        error(String.format(format!!, *args))
    }

    fun logStacktrace(e: Exception) {
        log(e.toString())
        for (el in e.stackTrace) {
            log(el.toString())
        }
    }
}