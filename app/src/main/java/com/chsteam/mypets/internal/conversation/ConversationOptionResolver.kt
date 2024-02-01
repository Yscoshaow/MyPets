package com.chsteam.mypets.internal.conversation

import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.internal.exceptions.InstructionParseException
import com.chsteam.mypets.internal.id.ConversationID


/**
 * Resolves a string to a [ConversationData] instance and the name of the option.
 */
class ConversationOptionResolver(
    currentPackage: QuestPackage,
    currentConversationName: String,
    optionType: ConversationData.OptionType,
    option: String
) {

    /**
     * The [ConversationData.OptionType] of the option.
     */
    private val optionType: ConversationData.OptionType

    /**
     * The package from which we are searching for the conversation.
     */
    private lateinit var pack: QuestPackage

    /**
     * The name of the option that is searched.
     */
    private var optionName: String? = null

    /**
     * The name of the conversation that is searched.
     */
    private lateinit var convName: String

    /**
     * Prepares the given information for resolving a conversation option inside a conversation.
     * Use [.resolve] to resolve the information.
     *
     * @param plugin                  the plugin instance
     * @param currentPackage          the package from which we are searching for the conversation
     * @param currentConversationName the current conversation data
     * @param optionType              the [ConversationData.OptionType] of the option
     * @param option                  the option string to resolve
     * @throws InstructionParseException when the option string is incorrectly formatted
     * @throws ObjectNotFoundException   when the conversation could not be found
     */
    init {
        this.optionType = optionType
        val parts = option.split("\\.".toRegex()).toTypedArray()
        when (parts.size) {
            3 -> {
                val conversationID = ConversationID(currentPackage, parts[0] + "." + parts[1])
                pack = conversationID.pack
                convName = parts[1]
                optionName = if (parts[2].isEmpty()) null else parts[2]
            }

            2 -> {
                pack = currentPackage
                convName = parts[0]
                optionName = parts[1]
            }

            1 -> {
                pack = currentPackage
                convName = currentConversationName
                optionName = parts[0]
            }

            else -> throw InstructionParseException(
                ("Invalid conversation pointer format in package '"
                        + currentPackage.questPath + "', conversation '" + currentConversationName + "': " + option)
            )
        }
    }

    /**
     * Resolves the given information to a [ResolvedOption].
     *
     * @return a [ResolvedOption]
     * @throws ObjectNotFoundException when the conversation containing the option could not be found
     */
    fun resolve(): ResolvedOption {
        val conversationWithNextOption = ConversationID(pack, convName)

        //Since the conversation might be in another package we must load this again
        val newData: ConversationData = ConversationManager.getConversation(conversationWithNextOption)
        return ResolvedOption(newData, optionType, (optionName)!!)
    }
}