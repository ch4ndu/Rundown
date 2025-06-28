package com.udnahc.firefly

import android.content.Context

class ImmediateSync(private val appContext: Context) {

    fun startImmediateSync() {
        DailySyncWorker.scheduleImmediateSync(appContext)
    }
}