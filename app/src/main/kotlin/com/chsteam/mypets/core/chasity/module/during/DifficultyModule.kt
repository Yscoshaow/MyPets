package com.chsteam.mypets.core.chasity.module.during

import androidx.compose.runtime.Composable
import com.chsteam.mypets.core.chasity.IChasity
import com.chsteam.mypets.core.chasity.data.DifficultyData
import com.chsteam.mypets.core.chasity.data.IData
import com.chsteam.mypets.core.chasity.module.IChasityModule
import java.util.Date

class DifficultyModule(override val chasity: IChasity, private val multi: Double) : IChasityModule {

    constructor(chasity: IChasity,  difficulty: ChasityDifficulty) : this(chasity, difficulty.multi)

    override fun trySetTime(oldDate: Date, newDate: Date): Date {
        val div = newDate.time - oldDate.time
        val newDiv = (multi * div).toLong()
        return Date(oldDate.time + newDiv)
    }

    enum class ChasityDifficulty(val multi: Double) {
        EASY(0.7),
        NORMAL(1.0),
        HARD(2.0),
        HELL(3.0)
    }

    override fun convertToData(): IData {
        return DifficultyData(this.multi)
    }
}