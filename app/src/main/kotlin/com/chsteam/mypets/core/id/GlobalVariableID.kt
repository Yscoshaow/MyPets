package com.chsteam.mypets.core.id

import com.chsteam.mypets.api.config.quest.QuestPackage

class GlobalVariableID(questPackage: QuestPackage, identifier: String) : ID(questPackage, identifier) {
    override val rawInstruction: String
        get() = TODO("Not yet implemented")
}