package com.chsteam.mypets.core.chasity.data

import com.chsteam.mypets.core.chasity.IChasity
import com.chsteam.mypets.core.chasity.module.IChasityModule
import com.chsteam.mypets.core.chasity.module.during.MaxTimeModule
import java.util.Date

data class MaxTimeData(val maxDate: Date): IData {
    override fun covertToModule(iChasity: IChasity): IChasityModule {
        return MaxTimeModule(iChasity, maxDate)
    }
}
