package com.udnahc.firefly

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.lighthousegames.logging.logging

class BootReceiver : BroadcastReceiver() {
    private val log = logging()
    override fun onReceive(
        context: Context?,
        intent: Intent?
    ) {
        log.w { "init" }
        if (context != null && Intent.ACTION_BOOT_COMPLETED == intent!!.action) {
            log.w { "scheduleJob" }
            DailySyncWorker.scheduleDailySync(context)
        }
    }
}