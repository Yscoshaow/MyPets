package com.chsteam.mypets.core.database

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chsteam.mypets.core.conversation.Conversation
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChatViewModel : ViewModel(), KoinComponent {

    private val chatDao: ChatDao by inject()

    private val dialogDao: DialogDao by inject()

    val latestMessages = MutableLiveData<HashMap<Npc, Message>>()

    val allMessage = MutableLiveData<HashMap<Npc, MutableList<Message>>>(HashMap())

    val chattingNpc = mutableStateOf<Npc?>(null)

    var chattingConversation: Conversation? = null

    val responseMessage = mutableStateOf(emptyList<String>())

    init {
        loadLatestMessages()
    }

    suspend fun startChat() {
        chattingNpc.value?.let {  npc ->
            val dialog = dialogDao.getDialogByChatId(npc.id)
            if(dialog != null) {
                chattingConversation = Conversation.getOrCreateConversation(npc)
                chattingConversation?.forceStart(dialog.pointer)
            } else {
                chattingConversation = Conversation.getOrCreateConversation(npc)
                chattingConversation?.start(null)
            }
        }
    }

    fun addMessage(message: Message) {
        val currentNpc = chattingNpc.value ?: return
        val messages = allMessage.value?.get(currentNpc) ?: mutableListOf()
        messages.add(message)
        allMessage.value?.put(currentNpc, messages)
        allMessage.value = allMessage.value
    }

    fun addOption(text: String) {
        responseMessage.value = responseMessage.value + text
    }


    private fun loadLatestMessages() {
        viewModelScope.launch {
            val latestMessagesMap = HashMap<Npc, Message>()
            chatDao.getLatestMessagesForAllChats().forEach { message ->
                chatDao.getNpcByMessageId(message.messageId)?.let { npc ->
                    latestMessagesMap[npc] = message
                }
            }
            latestMessages.postValue(latestMessagesMap)
        }
    }
}
