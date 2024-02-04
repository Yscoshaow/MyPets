package com.chsteam.mypets.internal.id

import com.chsteam.mypets.api.config.quest.QuestPackage
import java.util.Objects


abstract class ID(var pack: QuestPackage, val identifier: String) {

    companion object {
        const val UP_STR = "_"

        val PATHS = listOf(
            "events", "conditions", "objectives", "variables",
            "conversations", "cancel"
        )
    }

    abstract val rawInstruction: String

    init {
        if(identifier.contains(".")) {
            val dotIndex = identifier.indexOf(".")
            val packName = identifier.substring(0, dotIndex)

            if(packName.startsWith(UP_STR + "-")) {

            } else if(pack.packName.startsWith("-")) {

            } else {
                val parts = identifier.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()

            }
        }
    }

    open fun getBaseID(): String {
        return identifier
    }

    open fun getFullID(): String {
        return pack.packName + "." + identifier
    }

    override fun toString(): String {
        return getFullID();
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        val other = other as ID
        return (Objects.equals(identifier, other.identifier)
                && Objects.equals(pack.questPath, other.pack.questPath))
    }

    override fun hashCode(): Int {
        return Objects.hash(identifier, pack.questPath)
    }


}