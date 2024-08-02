package com.chsteam.mypets.core.chasity

import com.chsteam.mypets.core.chasity.module.IChasityModule
import java.util.Date

class Chasity(private val startDate: Date,
              private var endDate: Date,
              override var hideTime: Boolean = false,
              override var showChangeReason: Boolean = true,
              override var hygieneOpen: Boolean = false
) : IChasity {

    private val modules: MutableList<IChasityModule> = mutableListOf()

    override fun lock(): Boolean {
        modules.forEach {
            if(!it.tryLock()) return false
        }
        return true
    }

    override fun unlock(): Boolean {
        modules.forEach {
            if(!it.tryUnlock()) return false
        }
        return true
    }

    override fun getTimeUntil(): Date {
        var resultData = this.endDate
        modules.forEach {
            resultData = it.tryGetTime(resultData)
        }
        return resultData
    }

    override fun setTime(date: Date, reason: String) {
        var currentDate = date
        modules.forEach {
            currentDate = it.trySetTime(this.endDate, currentDate)
        }
        this.endDate = currentDate

    }

    override fun addModule(module: IChasityModule) {
        modules.forEach {
            if(!it.tryAddModule(module)) return
        }
        this.modules.add(module)
    }
}