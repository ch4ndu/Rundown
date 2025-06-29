/*
 * Copyright (c) 2025 https://github.com/ch4ndu
 *
 *  This file is part of Rundown (https://github.com/ch4ndu/Rundown).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see https://www.gnu.org/licenses/.
 */

package com.udnahc.firefly

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import data.AppPref
import data.database.serializers.DateSerializer
import domain.repository.ConnectivityStateManager
import domain.repository.HealthCheck
import domain.usecase.SyncStatus
import domain.usecase.SyncWithServerUseCase
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime
import org.lighthousegames.logging.logging
import java.time.Duration
import java.util.Calendar
import java.util.concurrent.TimeUnit

class DailySyncWorker(
    private val appContext: Context,
    workerParams: WorkerParameters,
    private val appPref: AppPref,
    private val connectivityStateManager: ConnectivityStateManager,
    private val syncWithServerUseCase: SyncWithServerUseCase,
) : CoroutineWorker(appContext, workerParams) {

    private val log = logging()

    override suspend fun doWork(): Result {
        val startedAt = """startedAt-${
            Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
                .format(DateSerializer.displayFormat)
        }"""
        try {
            setForeground(getForegroundInfo())
        } catch (exception: Exception) {
            scheduleDailySync(appContext)
            log.e(exception) { "wtf" }
        }
        if (connectivityStateManager.isConnectedToServer.value == HealthCheck.ExpiredToken) {
            log.d { "expiredToken. Need to ReLogin" }
            appPref.setLastSyncedAt("$startedAt::expiredToken-skipping")
            scheduleDailySync(appContext)
            return Result.success()
        }
        val syncStatus = syncWithServerUseCase.invoke()
        when (syncStatus) {
            is SyncStatus.SyncSuccess -> {
                scheduleDailySync(appContext)
                return Result.success()
            }

            is SyncStatus.SyncFailed -> return Result.failure()
        }
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                123432,
                createNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(
                123432,
                createNotification()
            )
        }
    }

    private fun createNotification(): Notification {
        createNotificationChannel()
        return NotificationCompat.Builder(appContext, "Sync")
            .setChannelId("SyncChannel")
            .setSmallIcon(R.drawable.ic_chart)
            .setContentTitle("Sync in progress")
            .setContentText("")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(null)
            .setAutoCancel(false)
            .build()
    }

    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("SyncChannel", "FireFly", importance).apply {
            description = "Sync Transactions"
        }
        // Register the channel with the system.
        val notificationManager: NotificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {

        private const val DAILY_SYNC = "DailySync"
        const val IMMEDIATE_SYNC = "immediateSync"
        private val log = logging()

        fun scheduleDailySync(context: Context) {
            log.d { "scheduleDailySync" }
            WorkManager.getInstance(context).cancelUniqueWork(DAILY_SYNC)
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresDeviceIdle(false)
                .setRequiresCharging(false)
                .setTriggerContentMaxDelay(Duration.ofMinutes(10))
                .setRequiresBatteryNotLow(false)
                .setRequiresStorageNotLow(false)
                .build()

            val dueDate = Calendar.getInstance()
            val currentDate = Calendar.getInstance()
            dueDate.set(Calendar.HOUR_OF_DAY, 4) // 4AM everyday
            dueDate.set(Calendar.MINUTE, 0)
            dueDate.set(Calendar.SECOND, 0)
            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, 24)
            }
            val timeDiff = dueDate.timeInMillis.minus(currentDate.timeInMillis)

            WorkManager.getInstance(context).enqueueUniqueWork(
                DAILY_SYNC, ExistingWorkPolicy.REPLACE,
                OneTimeWorkRequestBuilder<DailySyncWorker>()
                    .setConstraints(constraints)
                    .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                    .build()
            )
        }

        fun scheduleImmediateSync(
            context: Context,
            refreshTokens: Boolean = false
        ) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val data = Data.Builder()
            data.putBoolean("refreshTokens", refreshTokens)
            val workRequest = OneTimeWorkRequestBuilder<DailySyncWorker>()
                .setConstraints(constraints)
                .setInputData(data.build())
                .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(IMMEDIATE_SYNC, ExistingWorkPolicy.REPLACE, workRequest)
        }
    }
}