package com.chsteam.mypets.core.events

import com.chsteam.mypets.api.Event
import com.chsteam.mypets.core.Instruction
import com.chsteam.mypets.core.config.QuestManager
import com.chsteam.mypets.core.id.ObjectiveID


class ObjectiveEvent(instruction: Instruction) : Event(instruction) {

    companion object {
        private val ACTION_LIST = listOf("start", "add", "delete", "remove", "complete", "finish")
    }

    /**
     * All objectives affected by this event.
     */
    private val objectives: List<ObjectiveID>

    /**
     * The action to do with the objectives.
     */
    private val action: String

    init {
        action = instruction.next()?.lowercase() ?: "";
        if(action !in ACTION_LIST) {
            // TODO THROW ERROR
        }

        objectives = instruction.getList<ObjectiveID?>(instruction::getObjective).filterNotNull()
    }


    override fun execute() {
        objectives.forEach { objectiveID ->
            val objective = QuestManager.getObjective(objectiveID)

            when(action.lowercase()) {
                "start", "add" -> {

                }
                "delete", "remove" -> {

                }
                "complete", "finish" -> {

                }
            }
        }
    }
}