package com.chsteam.mypets.core

import com.chsteam.mypets.api.Condition
import com.chsteam.mypets.api.Objective
import com.chsteam.mypets.api.Event
import com.chsteam.mypets.core.conditions.RealTimeCondition
import com.chsteam.mypets.core.conditions.TagCondition
import com.chsteam.mypets.core.events.ObjectiveEvent
import com.chsteam.mypets.core.events.RunEvent
import com.chsteam.mypets.core.events.conversation.ConversationEvent
import com.chsteam.mypets.core.events.logic.FirstEvent
import com.chsteam.mypets.core.events.logic.IfElseEvent
import com.chsteam.mypets.core.events.tag.TagEvent
import com.chsteam.mypets.core.objectives.SelfObjective
import com.chsteam.mypets.core.objectives.ShockObjective


object Registries {

    private val CONDITION_TYPES: HashMap<String, Class<out Condition>> = HashMap()

    private val OBJECTIVE_TYPES: HashMap<String, Class<out Objective>> = HashMap()
    private val EVENT_TYPES: HashMap<String, Class<out Event>> = HashMap()

    init {
        registerEvents("conversation", ConversationEvent::class.java)
        registerEvents("tag", TagEvent::class.java)
        registerEvents("first", FirstEvent::class.java)
        registerEvents("ifelse", IfElseEvent::class.java)
        registerEvents("objective", ObjectiveEvent::class.java)
        registerEvents("run", RunEvent::class.java)

        registerConditions("time", RealTimeCondition::class.java)
        registerConditions("tag", TagCondition::class.java)

        registerObjectives("self", SelfObjective::class.java)
        registerObjectives("shock", ShockObjective::class.java)
    }

    /**
     * Registers new condition classes by their names
     *
     * @param name           name of the condition type
     * @param conditionClass class object for the condition
     */
    private fun registerConditions(name: String, conditionClass: Class<out Condition>) {
        CONDITION_TYPES[name] = conditionClass
    }

    fun getConditionClass(name: String): Class<out Condition>? {
        return CONDITION_TYPES[name]
    }

    private fun registerEvents(name: String, eventClass: Class<out Event>) {
        EVENT_TYPES[name] = eventClass
    }

    fun getEventClass(name: String): Class<out Event>? {
        return EVENT_TYPES[name]
    }

    /**
     * Registers new objective classes by their names
     *
     * @param name           name of the objective type
     * @param objectiveClass class object for the objective
     */
    private fun registerObjectives(name: String, objectiveClass: Class<out Objective>) {
        OBJECTIVE_TYPES[name] = objectiveClass
    }

    fun getObjectiveClass(name: String): Class<out Objective>? {
        return OBJECTIVE_TYPES[name]
    }

    /**
     * @return the condition types map
     */
    fun getConditionTypes(): Map<String, Class<out Condition>> {
        return HashMap<String, Class<out Condition>>(CONDITION_TYPES)
    }

    /**
     * @return the objective types map
     */
    fun getObjectiveTypes(): Map<String, Class<out Objective>> {
        return HashMap<String, Class<out Objective>>(OBJECTIVE_TYPES)
    }

}