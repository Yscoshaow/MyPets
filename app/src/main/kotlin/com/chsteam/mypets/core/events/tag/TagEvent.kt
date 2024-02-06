package com.chsteam.mypets.core.events.tag

import com.chsteam.mypets.api.Event
import com.chsteam.mypets.core.Instruction

class TagEvent(instruction: Instruction) : Event(instruction) {
    override fun execute(): Boolean {
        TODO("Not yet implemented")
    }
}