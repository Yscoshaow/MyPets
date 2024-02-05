package com.chsteam.mypets.core.conversation

import com.chsteam.mypets.api.Condition
import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.core.id.ConditionID
import com.chsteam.mypets.core.id.ConversationID
import com.chsteam.mypets.core.id.EventID
import com.electronwill.nightconfig.core.Config


class ConversationData(
    val conversationID: ConversationID,
    private val config: Config
) {

    /**
     * The [QuestPackage] this conversation is in.
     */
    private val pack: QuestPackage = conversationID.pack

    /**
     * The name of this conversation.
     */
    private val convName: String = conversationID.getBaseID()

    /**
     * A map of the questers name in different languages.
     */
    private val quester: Map<String, String> = HashMap()

    /**
     * A map of the global conversation prefix in different languages.
     */
    private val prefix: Map<String, String> = HashMap()

    /**
     * All events that will be executed once the conversation has ended.
     */
    private val finalEvents: List<EventID> = ArrayList()

    /**
     * The NPC options that the conversation can start from.
     */
    val startingOptions: List<String>

    /**
     * All references made by this conversation's pointers to other conversations.
     */
    private val externalPointers: MutableList<CrossConversationReference> = mutableListOf()

    /**
     * A map of all things the NPC can say during this conversation.
     * The key is the option name that can be pointed to.
     */
    private val npcOptions: HashMap<String, ConversationOption> = hashMapOf()

    /**
     * A map of all things the player can say during this conversation.
     * The key is the option name that can be pointed to.
     */
    private var playerOptions: HashMap<String, ConversationOption> = hashMapOf()

    init {
        this.startingOptions = config.get("first")

        loadNPCOptions(config.get("NPC_options"))
        loadPlayerOptions(config.get("player_options"))
    }

    private fun loadNPCOptions(npcSection: Config) {
        npcSection.valueMap().keys.forEach {
            npcOptions[it] = ConversationOption(
                this.conversationID,
                it,
                OptionType.NPC,
                npcSection.get(it)
            )
        }
    }

    private fun loadPlayerOptions(playerSection: Config) {
        playerSection.valueMap().keys.forEach {
            playerOptions[it] = ConversationOption(
                this.conversationID,
                it,
                OptionType.PLAYER,
                playerSection.get(it)
            )
        }
    }

    fun getPointers(option: ResolvedOption): List<String> {
        val optionMaps: Map<String, ConversationOption>
        optionMaps = if (option.type == OptionType.NPC) {
            option.conversationData.npcOptions
        } else {
            option.conversationData.playerOptions
        }
        return optionMaps[option.name]!!.getPointers()
    }



    enum class OptionType(val identifier: String, val readable: String) {
        /**
         * Things the NPC says.
         */
        NPC("NPC_options", "NPC option"),

        /**
         * Options selectable by the player.
         */
        PLAYER("player_options", "player option")
    }

    private class ConversationOption(
        val conversationID: ConversationID,
        val optionName: String,
        val optionType: OptionType,
        val config: Config
    ) {

        /**
         * The [QuestPackage] in which the conversation this option belongs to is defined.
         */
        private val pack: QuestPackage = conversationID.pack

        /**
         * The name of the conversation this option belongs to.
         */
        private val conversationName: String = conversationID.identifier // TODO BASE ID


        /**
         * The inline prefix of the option.
         */
        private val inlinePrefix: Map<String, String> = HashMap()

        /**
         * A map of the text of the option in different languages.
         */
        private val text: Map<String, String> = HashMap()

        /**
         * Conditions that must be met for the option to be available.
         */
        private val conditions: List<ConditionID> = ArrayList()

        /**
         * Events that are triggered when the option is selected.
         */
        private val events: List<EventID> = ArrayList()

        /**
         * Other options that are available after this option is selected.
         */
        private val pointers: List<String>

        /**
         * Other options that this option extends from.
         */
        private val extendLinks: List<String>

        init {
            pointers = null!!
            extendLinks = null!!
        }

        fun getCondition(): List<ConditionID> {
            return getConditions(mutableListOf())
        }

        fun getConditions(optionPath: MutableList<String>): List<ConditionID> {
            if(optionPath.contains(optionName)) {
                return emptyList()
            }
            optionPath.add(optionName)

            val conditions = mutableListOf<ConditionID>()
            conditions.addAll(this.conditions)

            extendLinks.forEach { extend ->
                val resolvedExtend = ConversationOptionResolver(pack, conversationName, optionType, extend).resolve()

                val conversationData =  resolvedExtend.conversationData
                // TODO
            }

            return conditions.toList()
        }

        fun getEvents(): List<EventID> {
            return getEvents(mutableListOf())
        }

        fun getEvents(optionPath: MutableList<String>): List<EventID> {
            if(optionPath.contains(optionName)) {
                return emptyList()
            }
            optionPath.add(optionName)

            val events = mutableListOf<EventID>()
            events.addAll(this.events)

            extendLinks.forEach { extend ->
                val resolvedExtend = ConversationOptionResolver(pack, conversationName, optionType, extend).resolve()

                val conversationData =  resolvedExtend.conversationData
                // TODO
            }

            return events.toList()
        }


        fun getPointers(): List<String> {
            return getPointers(mutableListOf())
        }

        fun getPointers(optionPath: MutableList<String>): List<String> {
            if (optionPath.contains(optionName)) {
                return emptyList()
            }
            optionPath.add(optionName)
            val pointers = mutableListOf<String>()
            pointers.addAll(this.pointers)

            extendLinks.forEach { extend ->
                val resolvedExtend = ConversationOptionResolver(pack, conversationName, optionType, extend).resolve()

                val conversationData =  resolvedExtend.conversationData
                // TODO
            }

            return pointers.toList()
        }
    }
}