package com.chsteam.mypets.core.database

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import java.util.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Npc::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("npcId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Chat(
    @PrimaryKey(autoGenerate = true) val chatId: Int = 0,
    val npcId: Int,
    val npc: String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Chat::class,
            parentColumns = ["chatId"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["chatId"])]
)
data class Message(
    @PrimaryKey(autoGenerate = true) val messageId: Int = 0,
    val chatId: Int,
    val message: String,
    val date: Date,
    val sendSelf: Boolean = false
)

@Dao
interface ChatDao {
    // 插入聊天，返回生成的ID
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: Chat): Long

    // 插入消息
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: Message): Long

    // 查询特定聊天的所有消息
    @Query("SELECT * FROM Message WHERE chatId = :chatId")
    suspend fun getMessagesForChat(chatId: Int): List<Message>

    // 查询所有聊天
    @Query("SELECT * FROM Chat")
    suspend fun getAllChats(): List<Chat>

    @Query("""
        SELECT Message.* FROM Message
        INNER JOIN (
            SELECT chatId, MAX(date) AS MaxDate
            FROM Message
            GROUP BY chatId
        ) AS LatestMessage ON Message.chatId = LatestMessage.chatId AND Message.date = LatestMessage.MaxDate
    """)
    suspend fun getLatestMessagesForAllChats(): List<Message>

    @Query("""
        SELECT Npc.* FROM Npc
        INNER JOIN Chat ON Npc.id = Chat.npcId
        INNER JOIN Message ON Chat.chatId = Message.chatId
        WHERE Message.messageId = :messageId
    """)
    suspend fun getNpcByMessageId(messageId: Int): Npc?
}