package com.chsteam.mypets.core.conversation

import com.chsteam.mypets.api.Condition
import com.chsteam.mypets.api.Event
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

    private var data: ConversationData? = QuestManager.getConversation(conversationID)

    init {

    }

    fun printNPCText() {
        if (this.nextNPCOption != null) {
            val text = data?.getText("default", this.nextNPCOption!!)

            // TODO RESOLVE VARIABLES
            // CONTROL ADD MESSAGE TO CHATTING LIST
            npcEventRun(this.nextNPCOption!!)
        }
    }

    fun passPlayerAnswer(number: Int) {
        availablePlayerOptions[number]?.let { playerEventRun(it) }
    }

    private fun playerEventRun(playerOption: ResolvedOption) {
        data?.getEventIDs(playerOption, ConversationData.OptionType.PLAYER)?.forEach {
            if (it != null) {
                Event.event(it)
            }
        }
    }

    fun responsePrint(playerOption: ResolvedOption) {
        selectOption(resolvePointers(playerOption), false)
        printNPCText()
    }

    private fun resolvePointers(option: ResolvedOption): List<ResolvedOption> {
        val nextConvData = option.conversationData
        val rawPointers = nextConvData.getPointers(option)
        val pointers = mutableListOf<ResolvedOption>()
        rawPointers.forEach { rawPointer ->
            val type = if(option.type == ConversationData.OptionType.PLAYER ) ConversationData.OptionType.NPC else ConversationData.OptionType.PLAYER
            pointers.add(ConversationOptionResolver(nextConvData.pack, nextConvData.convName, type, rawPointer).resolve())
        }
        return pointers.toList()
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

    private fun selectOption(options: List<ResolvedOption>, force: Boolean = false) {
        val inputOptions = if (force) listOf(options[0]) else options

        nextNPCOption = null

        inputOptions.forEach { option ->
            if(option.name == null) {
                option.conversationData.startingOptions.forEach { startingOptionName ->
                    if(force || Condition.conditions(option.conversationData.getConditionIDs(startingOptionName, ConversationData.OptionType.NPC))) {
                        this.data = option.conversationData
                        this.nextNPCOption = ResolvedOption(option.conversationData, ConversationData.OptionType.NPC, startingOptionName)
                        return
                    }
                }
            } else {
                if(force || Condition.conditions(option.conversationData.getConditionIDs(option.name, ConversationData.OptionType.NPC))) {
                    this.data = option.conversationData
                    this.nextNPCOption = option
                    return
                }
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

    private fun npcEventRun(npcOption: ResolvedOption) {
        data?.getEventIDs(npcOption, ConversationData.OptionType.NPC)?.forEach {
            if (it != null) {
                Event.event(it)
            }
        }
    }

    private fun updateOptionShow(npcOption: ResolvedOption) {

    }
}