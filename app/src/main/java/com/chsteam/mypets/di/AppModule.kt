package com.chsteam.mypets.di

import org.koin.dsl.module
import androidx.room.Room
import com.chsteam.mypets.internal.bluetooth.BluetoothViewModel
import com.chsteam.mypets.internal.database.MyPetsDatabase
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
}

val appModule = module {
    single { androidApplication().applicationContext }
}