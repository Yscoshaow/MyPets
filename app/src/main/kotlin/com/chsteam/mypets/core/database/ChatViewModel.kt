package com.chsteam.mypets.core.database

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChatViewModel : ViewModel(), KoinComponent {

    private val chatDao: ChatDao by inject()

    val latestMessages = MutableLiveData<HashMap<Npc, Message>>()

    val allMessage = MutableLiveData<HashMap<Npc, MutableList<Message>>>()

    val chattingNpc = mutableStateOf<Npc?>(null)

    init {
        loadLatestMessages()
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
