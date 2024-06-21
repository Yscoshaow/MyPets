package com.chsteam.mypets.api

import com.chsteam.mypets.core.Instruction
import com.chsteam.mypets.core.config.QuestManager
import com.chsteam.mypets.core.id.ConditionID


abstract class Condition(val instruction: Instruction) {
    abstract suspend fun execute() : Boolean

    companion object {
        suspend fun conditions(conditions: Collection<ConditionID>): Boolean {
            conditions.forEach {
                if(!condition(it)) {
                    return false
                }
            }
            return true
        }

        suspend fun condition(conditionID: ConditionID): Boolean {
            QuestManager.getCondition(conditionID)?.let {
                return it.execute()
            }
            return false
        }
    }
}