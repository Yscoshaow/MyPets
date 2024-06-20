package com.chsteam.mypets.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chsteam.mypets.core.database.converters.DateConverters
import com.chsteam.mypets.core.database.converters.ListConverters

@TypeConverters(DateConverters::class, ListConverters::class)
@Database(entities = [Chat::class, Message::class, Npc::class, Post::class, Tag::class, Dialog::class], version = 1)
abstract class MyPetsDatabase : RoomDatabase() {

    abstract fun chatDao(): ChatDao

    abstract fun npcDao(): NpcDao

    abstract fun postDao(): PostDao

    abstract fun tagDao(): TagDao

    abstract fun dialogDao(): DialogDao

}