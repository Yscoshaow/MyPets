package com.chsteam.mypets.core.chasity.data

import com.chsteam.mypets.core.chasity.IChasity
import com.chsteam.mypets.core.chasity.module.IChasityModule
import com.chsteam.mypets.core.chasity.module.during.DifficultyModule

data class DifficultyData(val multi: Double) : IData {
    override fun covertToModule(iChasity: IChasity): IChasityModule {
        return DifficultyModule(iChasity, multi)
    }
}