package com.chsteam.mypets.core.chasity.module.during

import androidx.compose.runtime.Composable
import com.chsteam.mypets.core.chasity.IChasity
import com.chsteam.mypets.core.chasity.data.IData
import com.chsteam.mypets.core.chasity.module.IChasityModule
import java.util.Date

class MaxTimeModule(override val chasity: IChasity, private val maxDate: Date) : IChasityModule {

    override fun trySetTime(oldDate: Date, newDate: Date): Date {
        if(newDate.time - maxDate.time >= 0) return maxDate
        return newDate
    }

    override fun convertToData(): IData {
        TODO("Not yet implemented")
    }


}