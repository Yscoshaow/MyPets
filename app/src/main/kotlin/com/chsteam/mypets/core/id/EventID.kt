package com.chsteam.mypets.core.id

import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.core.config.QuestManager

class EventID(questPackage: QuestPackage, identifier: String) : ID(questPackage, identifier) {

    override val rawInstruction: String = QuestManager.getRawEvent(questPackage, identifier) ?: ""

}