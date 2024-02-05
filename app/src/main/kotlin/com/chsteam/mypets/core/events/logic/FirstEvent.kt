package com.chsteam.mypets.core.events.logic

import com.chsteam.mypets.api.Event
import com.chsteam.mypets.core.Instruction

class FirstEvent(instruction: Instruction) : Event(instruction) {
    override fun execute() {
        TODO("Not yet implemented")
    }
}