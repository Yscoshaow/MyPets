package com.chsteam.mypets.internal.id

import com.chsteam.mypets.api.config.quest.QuestPackage


class VariableID(questPackage: QuestPackage, identifier: String) : ID(questPackage, "%" + identifier.replace("%", "") + "%") {

    override val rawInstruction: String = identifier

    override fun getBaseID(): String {
        return rawInstruction
    }

    override fun getFullID(): String {
        return pack.questPath + "-" + getBaseID()
    }
}