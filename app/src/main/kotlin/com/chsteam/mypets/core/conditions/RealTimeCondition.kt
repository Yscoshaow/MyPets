package com.chsteam.mypets.core.conditions

import com.chsteam.mypets.api.Condition
import com.chsteam.mypets.core.Instruction
import com.chsteam.mypets.core.exceptions.InstructionParseException
import java.util.Calendar
import java.util.Date

class RealTimeCondition(instruction: Instruction) : Condition(instruction) {

    private val hoursMin: Int

    private val minutesMin: Int

    private val hoursMax: Int

    private val minutesMax: Int

    init {
        val theTime = instruction.next()!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (theTime.size != 2) {
            throw InstructionParseException("Wrong time format")
        }
        try {
            val timeMin: Array<String> = theTime[0].split(":").toTypedArray()
            val timeMax: Array<String> = theTime[1].split(":").toTypedArray()
            if (timeMin.size != 2 || timeMax.size != 2) {
                throw InstructionParseException("Could not parse time")
            }
            this.hoursMin = timeMin[0].toInt()
            this.minutesMin = timeMin[1].toInt()
            this.hoursMax = timeMax[0].toInt()
            this.minutesMax = timeMax[1].toInt()
        } catch (e: NumberFormatException) {
            throw InstructionParseException("Could not parse time", e)
        }
        if (hoursMax < 0 || hoursMax > 23) {
            throw InstructionParseException("Could not parse time")
        }
        if (hoursMin < 0 || hoursMin > 23) {
            throw InstructionParseException("Could not parse time")
        }
        if (minutesMax < 0 || minutesMax > 59) {
            throw InstructionParseException("Could not parse time")
        }
        if (minutesMin < 0 || minutesMin > 59) {
            throw InstructionParseException("Could not parse time")
        }
        if (hoursMin === hoursMax && minutesMin === minutesMax) {
            throw InstructionParseException("min and max time must be different")
        }
    }


    override suspend fun execute(): Boolean {
        val cal = Calendar.getInstance()
        val now = cal.time
        val startTime = atTime(cal, hoursMin, minutesMin)
        val endTime = atTime(cal, hoursMax, minutesMax)

        return if (startTime.before(endTime)) {
            now.after(startTime) && now.before(endTime)
        } else {
            now.after(startTime) || now.before(endTime)
        }
    }

    private fun atTime(day: Calendar, hours: Int, minutes: Int): Date {
        day.isLenient = false
        day.set(Calendar.HOUR_OF_DAY, hours)
        day.set(Calendar.HOUR, if (hours < 12) hours else hours - 12)
        day.set(Calendar.AM_PM, if (hours < 12) Calendar.AM else Calendar.PM)
        day.set(Calendar.MINUTE, minutes)
        day.set(Calendar.SECOND, 0)
        day.set(Calendar.MILLISECOND, 0)
        return day.time
    }

}