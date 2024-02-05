package com.chsteam.mypets.core.events

import com.chsteam.mypets.api.Event
import com.chsteam.mypets.core.Instruction
import com.chsteam.mypets.core.config.QuestManager
import com.chsteam.mypets.core.id.EventID


class RunEvent(instruction: Instruction) : Event(instruction) {

    private val internalEvents: Array<Event>

    init {
        val parts = instruction.instruction.substring(3).trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val tempList = mutableListOf<Event>()
        var builder = StringBuilder()
        for (part in parts) {
            if (part.isNotEmpty() && part[0] == '^') {
                if (builder.isNotEmpty()) {
                    createEvent(builder.toString().trim { it <= ' ' })?.let { tempList.add(it) }
                    builder = StringBuilder()
                }
                builder.append(part.substring(1)).append(' ')
            } else {
                builder.append(part).append(' ')
            }
        }
        createEvent(builder.toString().trim { it <= ' ' })?.let { tempList.add(it) }

        internalEvents = tempList.toTypedArray()
    }

    private fun createEvent(instruction: String): Event? {
        val parts = instruction.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

        return QuestManager.getEvent(EventID(this.instruction.pack, parts[0]))
    }

    override fun execute() {
        internalEvents.forEach {
            it.execute()
        }
    }
}