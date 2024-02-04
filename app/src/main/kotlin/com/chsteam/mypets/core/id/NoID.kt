package com.chsteam.mypets.core.id

import com.chsteam.mypets.api.config.quest.QuestPackage


class NoID(pack: QuestPackage) : ID(pack, "no-id") {
    override val rawInstruction: String
        get() = "no-id"
}