package com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data

import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.DataOverflowException
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.OpenDGLab.toInt888

data class Power(val power: ByteArray) {

    constructor(a: Int, b: Int) : this(power = ByteArray(3)) {
        if (a < 0 || a > 2047) throw DataOverflowException()
        if (b < 0 || b > 2047) throw DataOverflowException()
        val i = 0 or ((b and 0x7FF) shl 11) or (a and 0x7FF)
        power[0] = (i and 0xFF).toByte()
        power[1] = ((i shr 8) and 0xFF).toByte()
        power[2] = ((i shr 16) and 0xFF).toByte()
    }

    var a: Int
        get() = power.toInt888() and 0x7FF
        set(value) {
            if (value < 0 || value > 2047) throw DataOverflowException()
            val i = 0 or ((b and 0x7FF) shl 11) or (value and 0x7FF)
            power[0] = (i and 0xFF).toByte()
            power[1] = ((i shr 8) and 0xFF).toByte()
            power[2] = ((i shr 16) and 0xFF).toByte()
        }

    var b: Int
        get() = (power.toInt888() shr 11) and 0x7FF
        set(value) {
            if (value < 0 || value > 2047) throw DataOverflowException()
            val i = 0 or ((value and 0x7FF) shl 11) or (a and 0x7FF)
            power[0] = (i and 0xFF).toByte()
            power[1] = ((i shr 8) and 0xFF).toByte()
            power[2] = ((i shr 16) and 0xFF).toByte()
        }

    fun getAB() = Pair(a, b)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Power

        if (!power.contentEquals(other.power)) return false

        return true
    }

    override fun hashCode(): Int {
        return power.contentHashCode()
    }
}