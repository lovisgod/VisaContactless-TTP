package com.lovisgod.testVisaTTP.handlers

import android.os.Handler
import android.os.Looper

object TimerManager {

    private var handler: Handler? = null
    private var callback: TimerCallback? = null

    fun startTimer() {
        handler = Handler(Looper.getMainLooper())
        handler?.postDelayed(runnable, 30000) // 30 seconds
    }

    fun stopTimer() {
        handler?.removeCallbacks(runnable)
    }

    fun setTimerCallback(callback: TimerCallback) {
        this.callback = callback
    }

    private val runnable = Runnable {
        callback?.onTimerFinished()
    }

    interface TimerCallback {
        fun onTimerFinished()
    }
}
