package com.chsteam.mypets.internal.id

import com.chsteam.mypets.api.config.quest.QuestPackage


class NoID(pack: QuestPackage) : ID(pack, "no-id") {
    override val rawInstruction: String
        get() = "no-id"
}