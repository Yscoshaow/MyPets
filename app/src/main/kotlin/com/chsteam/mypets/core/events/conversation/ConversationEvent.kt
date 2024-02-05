package com.chsteam.mypets.core.events.conversation

import com.chsteam.mypets.api.Event
import com.chsteam.mypets.core.Instruction

class ConversationEvent(instruction: Instruction) : Event(instruction) {

    init {

    }

    override fun execute() {
        TODO("Not yet implemented")
    }
}