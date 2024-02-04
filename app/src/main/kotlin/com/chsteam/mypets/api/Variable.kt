package com.chsteam.mypets.api

import com.chsteam.mypets.internal.Instruction

class Variable(val instruction: Instruction) {

    override fun toString(): String {
        return this.instruction.instruction
    }

}