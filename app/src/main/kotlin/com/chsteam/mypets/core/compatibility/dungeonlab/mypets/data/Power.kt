package com.chsteam.mypets.core.compatibility.dungeonlab.mypets.data

import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.DataOverflowException
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data.Power

data class Power(val power: ByteArray) {

    constructor(a: Int, b: Int) : this(power = ByteArray(2)) {
        if (a < 0 || a > 200) throw DataOverflowException()
        if (b < 0 || b > 200) throw DataOverflowException()
        power[0] = a.toByte()
        power[1] = b.toByte()
    }

    var a: Int
        get() = power[0].toInt()
        set(value) {
            if (value < 0 || value > 200) throw DataOverflowException()
            power[0] = a.toByte()
        }

    var b: Int
        get() = power[1].toInt()
        set(value) {
            if (value < 0 || value > 200) throw DataOverflowException()
            val i = 0 or ((value and 0x7FF) shl 11) or (a and 0x7FF)
            power[1] = b.toByte()
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
