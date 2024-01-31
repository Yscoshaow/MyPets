package com.chsteam.mypets.internal.database

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChatViewModel : ViewModel(), KoinComponent {

    private val chatDao: ChatDao by inject()

    val latestMessages = MutableLiveData<HashMap<Npc, Message>>()

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
