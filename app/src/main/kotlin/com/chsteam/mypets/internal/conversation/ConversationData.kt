package com.chsteam.mypets.internal.conversation

import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.internal.exceptions.InstructionParseException
import com.chsteam.mypets.internal.id.ConditionID
import com.chsteam.mypets.internal.id.ConversationID
import com.chsteam.mypets.internal.id.EventID


class ConversationData(
    val conversationID: ConversationID,
    val pack: QuestPackage,
    val convName: String,
    val finalEvents: List<EventID>,
    private val config: Map<String, Any>
) {

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
        if(config["quester"] == null) {
            throw InstructionParseException("The 'quester' name is missing in the conversation file!");
        }


    }

    private fun loadNPCOptions(conv: Map<String, Any>) {
        val npcSection: Map<String, Any> = conv["NPC_options"] as Map<String, Any>
        npcSection.forEach {
            npcOptions[it.key] = ConversationOption(
                this.conversationID,
                it.key,
                OptionType.NPC,
                it.value as Map<String, String>
            )
        }
    }

    private fun loadPlayerOptions(conv: Map<String, Any>) {
        val playerSection: Map<String, Any> = conv["player_options"] as Map<String, Any>
        playerSection.forEach {
            playerOptions[it.key] = ConversationOption(
                this.conversationID,
                it.key,
                OptionType.PLAYER,
                playerSection[it.key] as Map<String, String>
            )
        }
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
        val config: Map<String, String>
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

    }
}