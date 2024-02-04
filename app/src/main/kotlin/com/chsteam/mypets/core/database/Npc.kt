package com.chsteam.mypets.core.database

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import com.chsteam.mypets.pages.Utils

@Entity(indices = [Index(value = ["pack", "npcKey"], unique = true)])
data class Npc(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pack: String,
    val npcKey: String,
    val name: String,
    val avatar: String,
    val description: List<String>
) {
    @Composable
    fun ShowAvatar(modifier: Modifier = Modifier, size: Int = 7) {
        Utils.loadAvatarFromAssets(assetPath = avatar, modifier, size)
    }

}

@Dao
interface NpcDao {

    // 插入一个Npc对象到数据库中
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNpc(npc: Npc): Long

    // 根据名字查询Npc的avatar
    @Query("SELECT avatar FROM Npc WHERE name = :npcName")
    suspend fun getAvatarByNpcName(npcName: String): String?

    // 根据主键chatId查询Npc对象
    @Query("SELECT * FROM Npc WHERE id = :chatId")
    suspend fun getNpcById(chatId: Int): Npc?

    @Query("SELECT * FROM Npc WHERE pack = :pack AND npcKey = :key")
    suspend fun getNpcByPackAndName(pack: String, key: String): Npc?
}