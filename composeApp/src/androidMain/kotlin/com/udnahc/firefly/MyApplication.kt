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

import android.app.Application
import di.allModules
import di.dispatcherProvider
import di.initKoin
import di.networkModule
import di.platformModule
import di.sharedModule
import di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.lighthousegames.logging.KmLogging
import org.lighthousegames.logging.LogLevel
import org.lighthousegames.logging.PlatformLogger
import org.lighthousegames.logging.VariableLogLevel

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
            modules(allModules)
            workManagerFactory()
        }

//        if(BuildConfig.DEBUG) {
        KmLogging.setLoggers(PlatformLogger(VariableLogLevel(LogLevel.Verbose)))
//        } else {
//            KmLogging.setLoggers(PlatformLogger(VariableLogLevel(LogLevel.Warn)))
//        }
            DailySyncWorker.scheduleDailySync(this)
    }
}