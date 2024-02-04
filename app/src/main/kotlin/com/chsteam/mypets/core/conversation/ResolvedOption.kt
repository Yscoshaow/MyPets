package com.chsteam.mypets.core.conversation

/**
 * Simple record that represents one option inside a conversation.
 *
 * @param conversationData the data of the conversation that contains the option
 * @param type             the [com.chsteam.mypets.core.conversation.ConversationData.OptionType] of the option
 * @param name             the name of the option as defined in the conversation config
 */
data class ResolvedOption(
    val conversationData: ConversationData,
    val type: ConversationData.OptionType,
    val name: String
)