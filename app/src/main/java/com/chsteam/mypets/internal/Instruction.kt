package com.chsteam.mypets.internal

import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.internal.utils.split
import java.util.regex.Pattern

class Instruction(questPackage: QuestPackage, private val id: String, private val instruction: String) {

    private val parts: Array<String> = split(instruction)
    private var nextIndex = 1
    private var currentIndex = 1
    private var lastOptional: String = ""

    companion object {
        private val NUMBER_PATTERN: Pattern = Pattern.compile("(?:\\s|\\G|^)(([+\\-])?\\d+)(?:\\s|$)")
    }

    override fun toString(): String {
        return instruction
    }

    fun getInstruction(): String {
        return instruction
    }



}