package com.chsteam.mypets.core.chasity.data

import com.chsteam.mypets.core.chasity.IChasity
import com.chsteam.mypets.core.chasity.module.IChasityModule
import com.chsteam.mypets.core.chasity.module.during.RandomTimeModule

data class RandomTimeData(val minMulti: Double, val maxMulti: Double) : IData {
    override fun covertToModule(iChasity: IChasity): IChasityModule {
        return RandomTimeModule(iChasity, minMulti, maxMulti)
    }
}
