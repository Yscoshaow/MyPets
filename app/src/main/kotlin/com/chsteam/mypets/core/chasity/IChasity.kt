package com.chsteam.mypets.core.chasity

import com.chsteam.mypets.core.chasity.module.IChasityModule
import java.util.Date

interface IChasity {

    var hideTime: Boolean

    var showChangeReason: Boolean

    var hygieneOpen: Boolean

    fun lock() : Boolean

    fun unlock() : Boolean

    fun getTimeUntil() : Date

    fun setTime(date: Date, reason: String)

    fun addModule(module: IChasityModule)
}