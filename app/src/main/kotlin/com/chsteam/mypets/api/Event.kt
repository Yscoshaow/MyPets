package com.chsteam.mypets.api

import com.chsteam.mypets.core.Instruction
import com.chsteam.mypets.core.id.ConditionID


abstract class Event(val instruction: Instruction) {

    protected val conditions: Array<ConditionID>

    init {
        val tempConditions1 = instruction.getArray(instruction.getOptional("condition"))
        val tempConditions2 = instruction.getArray(instruction.getOptional("conditions"))
        val size = tempConditions1.size + tempConditions2.size
        val tempList = mutableListOf<ConditionID>()
        for (i in 0 until size) {
            val condition = if (i >= tempConditions1.size) tempConditions2[i - tempConditions1.size]!! else tempConditions1[i]!!
            tempList.add(ConditionID(instruction.pack, condition))
        }
        conditions = tempList.toTypedArray()
    }

    abstract fun execute()

    /**
     * Returns the full id of this event. This includes the package path and the event name, seperated by a dot.
     *
     * @return the full id of this event
     */
    protected open fun getFullId(): String {
        return instruction.identifier.getFullID()
    }

}