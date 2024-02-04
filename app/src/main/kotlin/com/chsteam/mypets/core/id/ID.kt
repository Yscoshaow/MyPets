package com.chsteam.mypets.core.id

import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.core.Instruction
import com.chsteam.mypets.core.config.QuestManager.Companion.getQuestPackage
import java.util.Objects


abstract class ID(pack: QuestPackage?, val identifier: String) {

    companion object {
        const val UP_STR = "_"

        val PATHS = listOf(
            "events", "conditions", "objectives", "variables",
            "conversations", "cancel"
        )
    }

    abstract val rawInstruction: String

    protected var instruction: Instruction? = null

    var pack: QuestPackage

    init {
        if(identifier.contains(".")) {
            var dotIndex = identifier.indexOf(".")
            val packName = identifier.substring(0, dotIndex)
            if(pack != null && packName.startsWith("$UP_STR-")) {
                resolveRelativePathUp(pack, identifier, packName)
            } else if(pack != null && packName.startsWith("-")) {
                resolveRelativePathDown(pack, identifier, packName)
            } else {
                val parts = identifier.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()[0].split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                    .toTypedArray()

                val potentialPack = getQuestPackage(packName)
                if (potentialPack == null) {
                    this.pack = pack!!
                    dotIndex = -1
                } else {
                    // TODO MORE
                    this.pack = potentialPack
                }
            }

            this.pack = getQuestPackage(packName)!!
        } else {
            if(pack != null) {
                this.pack = pack
            } else {
               this.pack = QuestPackage("error", "error")
            }
        }
    }

    private  fun resolveRelativePathUp(
        pack: QuestPackage,
        identifier: String,
        packName: String
    ) {
        // resolve relative name if we have a supplied package
        val root = pack.questPath.split("-".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        val path = packName.split("-".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        // count how many packages up we need to go
        var stepsUp = 0
        while (stepsUp < path.size && UP_STR.equals(path[stepsUp])) {
            stepsUp++
        }
        val builder = StringBuilder()
        for (i in 0 until (root.size - stepsUp)) {
            builder.append(root[i]).append('-')
        }
        for (i in stepsUp until path.size) {
            builder.append(path[i]).append('-')
        }
        val absolute = builder.substring(0, builder.length - 1)
        this.pack = getQuestPackage(absolute)!!
    }

    private fun resolveRelativePathDown(
        pack: QuestPackage,
        identifier: String,
        packName: String
    ) {
        val currentPath = pack.questPath
        val fullPath = currentPath + packName
        this.pack = getQuestPackage(fullPath)!!
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

    fun generateInstruction(): Instruction {
        if (instruction == null) {
            instruction = Instruction(pack, this, rawInstruction)
        }
        return instruction!!
    }


}