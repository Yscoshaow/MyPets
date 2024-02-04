package com.chsteam.mypets.api

import com.chsteam.mypets.internal.Instruction




abstract class Condition(val instruction: Instruction) {
    abstract fun execute() : Boolean
}