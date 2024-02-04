package com.chsteam.mypets.internal.id

import com.chsteam.mypets.api.config.quest.QuestPackage

class ObjectiveID(questPackage: QuestPackage, identifier: String) : ID(questPackage, identifier) {

    override val rawInstruction: String = super.pack.getString("objectives." + super.identifier)

}