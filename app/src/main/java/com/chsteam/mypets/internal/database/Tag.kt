package com.chsteam.mypets.internal.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update

@Entity(
    tableName = "tags",
    indices = [Index(value = ["pack", "tag"], unique = true)]
)
data class Tag(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pack: String,
    val tag: String,
)

@Dao
interface TagDao {

    @Insert
    fun insertTags(vararg tags: Tag): List<Long>

    @Update
    fun updateTag(tag: Tag)

    @Delete
    fun deleteTag(tag: Tag)

    @Query("SELECT * FROM tags")
    fun getAllTags(): List<Tag>

    @Query("SELECT * FROM tags WHERE id = :id")
    fun getTagById(id: Int): Tag?

    @Query("SELECT * FROM tags WHERE pack = :pack")
    fun getTagsByPack(pack: String): List<Tag>

    @Query("DELETE FROM tags WHERE pack = :pack")
    fun deleteTagsByPack(pack: String)
}