package com.chsteam.mypets.core.id

import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.core.config.QuestManager

class ObjectiveID(questPackage: QuestPackage, identifier: String) : ID(questPackage, identifier) {

    override val rawInstruction: String = QuestManager.getRawObjective(questPackage, identifier) ?: ""

}