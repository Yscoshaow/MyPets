package com.chsteam.mypets.core.database

import androidx.room.PrimaryKey

data class Objective(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pack: String,
    val identifier: String,
)