package com.chsteam.mypets.core.database.converters

import androidx.room.TypeConverter

class ListConverters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split("YSC!").map { it.trim() }
    }

    @TypeConverter
    fun fromArrayList(list: List<String>): String {
        return list.joinToString("YSC!")
    }

}