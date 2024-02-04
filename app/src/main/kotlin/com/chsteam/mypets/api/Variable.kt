package com.chsteam.mypets.api

import com.chsteam.mypets.core.Instruction

class Variable(val instruction: Instruction) {

    override fun toString(): String {
        return this.instruction.instruction
    }

}