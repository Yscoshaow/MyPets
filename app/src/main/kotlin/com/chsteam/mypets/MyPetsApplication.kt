package com.chsteam.mypets

import android.app.Application
import com.chsteam.mypets.di.appModule
import com.chsteam.mypets.di.databaseModule
import com.chsteam.mypets.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyPetsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyPetsApplication)
            modules(listOf(databaseModule, viewModelModule, appModule))
        }
    }
}