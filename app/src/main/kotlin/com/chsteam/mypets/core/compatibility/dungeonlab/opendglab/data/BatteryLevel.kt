package com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data

data class BatteryLevel(val battery: ByteArray) {

    fun getLevel() = battery[0].toInt()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BatteryLevel

        if (!battery.contentEquals(other.battery)) return false

        return true
    }

    override fun hashCode(): Int {
        return battery.contentHashCode()
    }
}