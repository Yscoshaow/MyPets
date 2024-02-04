package com.chsteam.mypets.internal.conversation

import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.internal.config.QuestManager
import com.chsteam.mypets.internal.database.Npc
import com.chsteam.mypets.internal.id.ConversationID
import org.koin.core.KoinApplication.Companion.init
import java.util.concurrent.ConcurrentHashMap


class Conversation(conversationID: ConversationID, val startingOption: String) {

    companion object {
        private val ACTIVE_CONVERSATIONS: ConcurrentHashMap<Npc, Conversation> = ConcurrentHashMap<Npc, Conversation>()
    }

    private val pack: QuestPackage = conversationID.pack

    private val identifier: ConversationID = conversationID

    private val blackList: List<String>? = null

    private val conv: Conversation? = null

    private val availablePlayerOptions: Map<Int, ResolvedOption> = HashMap()

    protected var nextNPCOption: ResolvedOption? = null

    protected var state = ConversationState.CREATED

    private val data: ConversationData? = QuestManager.getConversation(conversationID)

    init {

    }
}