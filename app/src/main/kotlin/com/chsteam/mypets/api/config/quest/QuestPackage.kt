package com.chsteam.mypets.api.config.quest

class QuestPackage(val packName: String, val questPath: String) {
    fun getString(address: String): String {
        return "$packName.$address"
    }
}