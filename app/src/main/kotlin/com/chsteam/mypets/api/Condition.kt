package com.chsteam.mypets.api

import com.chsteam.mypets.core.Instruction




abstract class Condition(val instruction: Instruction) {
    abstract fun execute() : Boolean
}