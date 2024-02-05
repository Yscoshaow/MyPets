package com.chsteam.mypets.core.conversation

import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.core.config.QuestManager
import com.chsteam.mypets.core.database.Npc
import com.chsteam.mypets.core.id.ConversationID
import java.util.concurrent.ConcurrentHashMap


class Conversation(conversationID: ConversationID, val startingOption: String) {

    companion object {
        private val ACTIVE_CONVERSATIONS: ConcurrentHashMap<Npc, Conversation> = ConcurrentHashMap<Npc, Conversation>()

        fun getOrCreateConversation(npc: Npc) : Conversation {
            return if(ACTIVE_CONVERSATIONS[npc] != null) {
                ACTIVE_CONVERSATIONS[npc]!!
            } else {
                val questPackage = QuestManager.getQuestPackage(npc.pack)!!
                val conversationID = ConversationID(questPackage, npc.npcKey)
                val conversation = Conversation(conversationID, "")
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

    protected var state = ConversationState.CREATED

    private val data: ConversationData? = QuestManager.getConversation(conversationID)

    init {

    }
}