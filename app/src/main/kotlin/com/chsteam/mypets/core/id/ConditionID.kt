package com.chsteam.mypets.core.id

import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.core.config.QuestManager

class ConditionID(questPackage: QuestPackage, identifier: String) : ID(questPackage, removeExclamationMark(identifier)) {

    companion object {
        private fun removeExclamationMark(identifier: String): String {
            return if (identifier.isNotEmpty() && identifier[0] == '!') {
                identifier.substring(1)
            } else identifier
        }
    }

    val inverted: Boolean

    override val rawInstruction: String =  QuestManager.getRawCondition(questPackage, identifier) ?: ""

    init {
        this.inverted = !identifier.isEmpty() && identifier[0] == '!';
    }
}