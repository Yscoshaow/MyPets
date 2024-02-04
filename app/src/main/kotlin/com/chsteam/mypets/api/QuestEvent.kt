package com.chsteam.mypets.api

import com.chsteam.mypets.internal.Instruction

abstract class QuestEvent(val instruction: Instruction) {

    init {

    }

    abstract fun execute()
}