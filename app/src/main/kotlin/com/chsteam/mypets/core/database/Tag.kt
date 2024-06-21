package com.chsteam.mypets.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "tags")
data class Tag(
    @PrimaryKey val tag: String,
)

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tag: Tag)

    @Query("SELECT EXISTS(SELECT 1 FROM tags WHERE tag = :tag)")
    suspend fun contains(tag: String): Boolean

    @Delete
    suspend  fun delete(tag: Tag)
}