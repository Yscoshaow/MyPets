package com.chsteam.mypets.core.conversation

import com.chsteam.mypets.api.Condition
import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.core.config.QuestManager
import com.chsteam.mypets.core.database.Npc
import com.chsteam.mypets.core.id.ConversationID
import java.util.concurrent.ConcurrentHashMap


class Conversation(conversationID: ConversationID, var startingOption: List<String> = emptyList()) {

    companion object {
        private val ACTIVE_CONVERSATIONS: ConcurrentHashMap<Npc, Conversation> = ConcurrentHashMap<Npc, Conversation>()

        fun getOrCreateConversation(npc: Npc) : Conversation {
            return if(ACTIVE_CONVERSATIONS[npc] != null) {
                ACTIVE_CONVERSATIONS[npc]!!
            } else {
                val questPackage = QuestManager.getQuestPackage(npc.pack)!!
                val conversationID = ConversationID(questPackage, npc.npcKey)
                val conversation = Conversation(conversationID)
                ACTIVE_CONVERSATIONS[npc] = conversation
                conversation
            }
        }
    }

    private val pack: QuestPackage = conversationID.pack

    private val identifier: ConversationID = conversationID

    private val blackList: List<String>? = null

    private val conv: Conversation? = null

    private val availablePlayerOptions: Map<Int, ResolvedOption> = HashMap()

    protected var nextNPCOption: ResolvedOption? = null

    @Volatile
    protected var state = ConversationState.CREATED

    private val data: ConversationData? = QuestManager.getConversation(conversationID)

    init {

    }

    fun printNPCText()  {
        if(nextNPCOption == null) {
            return
        }


    }

    fun passPlayerAnswer(number: Int) {

    }


    fun start(startingOption: String) {
        val startingOptions = mutableListOf(startingOption)
        if (state.isStarted) {
            return
        }
        state = ConversationState.ACTIVE

        if(startingOption.isEmpty()) {
            startingOptions.addAll(data!!.startingOptions)
            val resolvedOptions = resolveOptions(startingOptions)
            selectOption(resolvedOptions, false)
        } else {
            selectOption(startingOptions)
        }
    }

    private fun selectOption(resolvedOptions: List<String>) {

    }

    private fun selectOption(options: List<ResolvedOption>, force: Boolean) {
        val inputOptions = if (force) listOf(options[0]) else options

        nextNPCOption = null

        inputOptions.forEach { option ->
            if(option.name == null) {
                option.conversationData.startingOptions.forEach {

                }
            } else {

            }

        }
    }

    private fun resolveOptions(startingOptions: List<String>) : List<ResolvedOption> {
        val tempList = mutableListOf<ResolvedOption>()
        startingOptions.forEach {
            val resolveOption = ConversationOptionResolver(pack, identifier.getBaseID(), ConversationData.OptionType.NPC, it).resolve()
            tempList.add(resolveOption)
        }
        return tempList.toList()
    }
}