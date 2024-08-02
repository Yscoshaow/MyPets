package com.chsteam.mypets.core.chasity.module

import androidx.compose.runtime.Composable
import com.chsteam.mypets.core.chasity.IChasity
import com.chsteam.mypets.core.chasity.data.IData
import java.util.Date

interface IChasityModule {

    val chasity: IChasity

    fun tryLock() : Boolean {
        return true
    }

    fun tryUnlock() : Boolean {
        return true
    }

    fun tryAddModule(module: IChasityModule) : Boolean {
        return true
    }

    fun trySetTime(oldDate: Date, newDate: Date) : Date {
        return newDate
    }

    fun tryGetTime(data: Date) : Date {
        return data
    }

    fun convertToData() : IData
}