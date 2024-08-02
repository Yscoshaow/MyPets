package com.chsteam.mypets.core.chasity.module.during

import androidx.compose.runtime.Composable
import com.chsteam.mypets.core.chasity.IChasity
import com.chsteam.mypets.core.chasity.data.IData
import com.chsteam.mypets.core.chasity.module.IChasityModule
import java.util.Date
import kotlin.random.Random

class RandomTimeModule(override val chasity: IChasity, private val minMulti: Double, private val maxMulti: Double) : IChasityModule {

    constructor(chasity: IChasity, maxMulti: Double) : this(chasity, 0.0, maxMulti)

    override fun trySetTime(oldDate: Date, newDate: Date): Date {
        val div = newDate.time - oldDate.time
        val random = Random.nextDouble(minMulti, maxMulti)
        val newDiv = (random * div).toLong()
        return Date(oldDate.time + newDiv)
    }

    override fun convertToData(): IData {
        TODO("Not yet implemented")
    }

}