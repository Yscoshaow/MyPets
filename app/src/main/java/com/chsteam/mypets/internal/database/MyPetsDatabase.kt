package com.chsteam.mypets.internal.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chsteam.mypets.internal.database.converters.DateConverters

@TypeConverters(DateConverters::class)
@Database(entities = [Chat::class, Message::class, Npc::class], version = 1)
abstract class MyPetsDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao

    abstract fun npcDao(): NpcDao
}