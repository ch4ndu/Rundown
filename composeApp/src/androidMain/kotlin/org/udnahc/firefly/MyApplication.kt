package org.udnahc.firefly

import android.app.Application
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
            modules(dispatcherProvider, sharedModule, platformModule, networkModule, viewModelModule)
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