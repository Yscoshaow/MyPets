package com.chsteam.mypets.internal.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@Entity
data class Npc(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val avatar: String
) {

    @Ignore
    var imageBitmap: MutableState<ImageBitmap> = mutableStateOf(createBlankImageBitmap())
        private set

    init {
        CoroutineScope(Dispatchers.IO).launch {
            imageBitmap.value = getAvatarImageBitmap()
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private suspend fun getAvatarImageBitmap(): ImageBitmap {
        // 使用协程处理图片加载
        return try {
            val bytes = Base64.decode(avatar)
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            bitmap.asImageBitmap()
        } catch (e: Exception) {
            createBlankImageBitmap()
        }
    }

    private fun createBlankImageBitmap(): ImageBitmap {
        val bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        bitmap.eraseColor(android.graphics.Color.TRANSPARENT) // 设置为透明
        return bitmap.asImageBitmap()
    }
}

@Dao
interface NpcDao {

    // 插入一个Npc对象到数据库中
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNpc(npc: Npc): Long

    // 根据名字查询Npc的avatar
    @Query("SELECT avatar FROM Npc WHERE name = :npcName")
    suspend fun getAvatarByNpcName(npcName: String): String?

    // 根据主键chatId查询Npc对象
    @Query("SELECT * FROM Npc WHERE id = :chatId")
    suspend fun getNpcById(chatId: Int): Npc?
}