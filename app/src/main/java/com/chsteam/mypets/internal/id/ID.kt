package com.chsteam.mypets.internal.id

import com.chsteam.mypets.api.config.quest.QuestPackage


abstract class ID(val questPackage: QuestPackage, val identifier: String) {

    companion object {
        const val UP_STR = "_"

        val PATHS = listOf(
            "events", "conditions", "objectives", "variables",
            "conversations", "cancel"
        )
    }

}