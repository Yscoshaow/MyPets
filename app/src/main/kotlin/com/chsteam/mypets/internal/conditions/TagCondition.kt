package com.chsteam.mypets.internal.conditions

import com.chsteam.mypets.api.Condition
import com.chsteam.mypets.internal.Instruction
import com.chsteam.mypets.internal.utils.addPackage

class TagCondition(instruction: Instruction) : Condition(instruction) {

    private val tag: String

    init {
        tag = addPackage(instruction.pack, instruction.instruction)
    }

    override fun execute(): Boolean {
        TODO("Not yet implemented")
    }
}