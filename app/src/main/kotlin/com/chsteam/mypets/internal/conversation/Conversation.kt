package com.chsteam.mypets.internal.conversation

import com.chsteam.mypets.internal.database.Npc
import com.chsteam.mypets.internal.id.ConversationID
import java.util.concurrent.ConcurrentHashMap


class Conversation {
    companion object {
        private val ACTIVE_CONVERSATIONS: ConcurrentHashMap<Npc, Conversation> = ConcurrentHashMap<Npc, Conversation>()
    }

    private val identifier: ConversationID? = null

    private val blackList: List<String>? = null

    private val conv: Conversation? = null

    private val availablePlayerOptions: Map<Int, ResolvedOption> = HashMap<Int, ResolvedOption>()

    protected var nextNPCOption: ResolvedOption? = null


}