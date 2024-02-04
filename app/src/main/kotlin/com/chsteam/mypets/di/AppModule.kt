package com.chsteam.mypets.di

import org.koin.dsl.module
import androidx.room.Room
import com.chsteam.mypets.core.bluetooth.BluetoothViewModel
import com.chsteam.mypets.core.database.ChatViewModel
import com.chsteam.mypets.core.database.MyPetsDatabase
import com.chsteam.mypets.core.experimental.ExperimentalViewModel
import org.koin.android.ext.koin.androidApplication

val databaseModule = module {
    single {
        Room.databaseBuilder(get(), MyPetsDatabase::class.java, "mypets_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<MyPetsDatabase>().chatDao() }
    single { get<MyPetsDatabase>().npcDao() }
}

val viewModelModule = module {
    single { BluetoothViewModel() }
    single { ChatViewModel() }
    single { ExperimentalViewModel() }
}

val appModule = module {
    single { androidApplication().applicationContext }
}