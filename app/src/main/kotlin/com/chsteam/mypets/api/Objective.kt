package com.chsteam.mypets.api

import androidx.compose.runtime.Composable
import com.chsteam.mypets.core.Instruction
import com.chsteam.mypets.core.id.ConditionID
import com.chsteam.mypets.core.id.EventID


abstract class Objective(val instruction: Instruction) {

    abstract val typeName: String

    protected val events: Array<EventID>

    protected val conditions: Array<ConditionID>

    init {
        val tempEvents1 = instruction.getArray(instruction.getOptional("event"))
        val tempEvents2 = instruction.getArray(instruction.getOptional("events"))

        val sizeEvent = tempEvents1.size + tempEvents2.size

        val tempListEvent = mutableListOf<EventID>()

        for (i in 0 until sizeEvent ) {
            val event = if (i >= tempEvents1.size) tempEvents2[i - tempEvents1.size]!! else tempEvents1[i]!!
            tempListEvent.add(EventID(instruction.pack, event))
        }
        events = tempListEvent.toTypedArray()

        val tempConditions1 = instruction.getArray(instruction.getOptional("condition"))
        val tempConditions2 = instruction.getArray(instruction.getOptional("conditions"))
        val sizeCondition = tempConditions1.size + tempConditions2.size
        val tempListCondition = mutableListOf<ConditionID>()
        for (i in 0 until sizeCondition) {
            val condition = if (i >= tempConditions1.size) tempConditions2[i - tempConditions1.size]!! else tempConditions1[i]!!
            tempListCondition.add(ConditionID(instruction.pack, condition))
        }
        conditions = tempListCondition.toTypedArray()

    }

    @Composable
    abstract fun TaskCard()
}