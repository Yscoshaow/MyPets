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
import kotlin.reflect.KClass


object Registries {

    private val CONDITION_TYPES: HashMap<String, KClass<out Condition>> = HashMap()

    private val OBJECTIVE_TYPES: HashMap<String, KClass<out Objective>> = HashMap()
    private val EVENT_TYPES: HashMap<String, KClass<out Event>> = HashMap()

    init {
        registerEvents("conversation", ConversationEvent::class)
        registerEvents("tag", TagEvent::class)
        registerEvents("first", FirstEvent::class)
        registerEvents("ifelse", IfElseEvent::class)
        registerEvents("objective", ObjectiveEvent::class)
        registerEvents("run", RunEvent::class)

        registerConditions("time", RealTimeCondition::class)
        registerConditions("tag", TagCondition::class)

        registerObjectives("self", SelfObjective::class)
        registerObjectives("shock", ShockObjective::class)
    }

    /**
     * Registers new condition classes by their names
     *
     * @param name           name of the condition type
     * @param conditionClass class object for the condition
     */
    private fun registerConditions(name: String, conditionClass: KClass<out Condition>) {
        CONDITION_TYPES[name] = conditionClass
    }

    fun getConditionClass(name: String): KClass<out Condition>? {
        return CONDITION_TYPES[name]
    }

    private fun registerEvents(name: String, eventClass: KClass<out Event>) {
        EVENT_TYPES[name] = eventClass
    }

    fun getEventClass(name: String): KClass<out Event>? {
        return EVENT_TYPES[name]
    }

    /**
     * Registers new objective classes by their names
     *
     * @param name           name of the objective type
     * @param objectiveClass class object for the objective
     */
    private fun registerObjectives(name: String, objectiveClass: KClass<out Objective>) {
        OBJECTIVE_TYPES[name] = objectiveClass
    }

    fun getObjectiveClass(name: String): KClass<out Objective>? {
        return OBJECTIVE_TYPES[name]
    }

    /**
     * @return the condition types map
     */
    fun getConditionTypes(): Map<String, KClass<out Condition>> {
        return HashMap<String, KClass<out Condition>>(CONDITION_TYPES)
    }

    /**
     * @return the objective types map
     */
    fun getObjectiveTypes(): Map<String, KClass<out Objective>> {
        return HashMap<String, KClass<out Objective>>(OBJECTIVE_TYPES)
    }

}