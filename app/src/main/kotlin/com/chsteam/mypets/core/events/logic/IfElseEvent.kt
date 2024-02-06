package com.chsteam.mypets.core.events.logic

import com.chsteam.mypets.api.Event
import com.chsteam.mypets.core.Instruction

class IfElseEvent(instruction: Instruction) : Event(instruction) {

    init {

    }

    override fun execute(): Boolean {
        TODO("Not yet implemented")
    }
}