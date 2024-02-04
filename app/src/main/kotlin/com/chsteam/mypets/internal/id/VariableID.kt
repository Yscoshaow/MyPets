package com.chsteam.mypets.internal.id

import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.internal.Instruction
import org.koin.core.KoinApplication.Companion.init


class VariableID(questPackage: QuestPackage, identifier: String) : ID(questPackage, "%" + identifier.replace("%", "") + "%") {

    override val rawInstruction: String = identifier

    override fun getBaseID(): String {
        return rawInstruction
    }

    override fun getFullID(): String {
        return pack.questPath + "-" + getBaseID()
    }
}