package com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.data

import com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.DataOverflowException
import com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.OpenDGLab.toInt888

data class WaveData(val wave: ByteArray) {
    constructor(x: Int, y: Int, z: Int) : this(wave = ByteArray(3)) {
        if (x < 0 || x > 31) throw DataOverflowException()
        if (y < 0 || y > 1023) throw DataOverflowException()
        if (z < 0 || z > 31) throw DataOverflowException()
        val i = 0 or ((z and 0x1F) shl 15) or ((y and 0x3FF) shl 5) or (x and 0x1F)
        wave[0] = (i and 0xFF).toByte()
        wave[1] = ((i shr 8) and 0xFF).toByte()
        wave[2] = ((i shr 16) and 0xFF).toByte()
    }
    var x: Int
        get() = wave.toInt888() and 0x1F
        set(value) {
            if (value < 0 || value > 31) throw DataOverflowException()
            val i = 0 or ((z and 0x1F) shl 15) or ((y and 0x3FF) shl 5) or (value and 0x1F)
            wave[0] = (i and 0xFF).toByte()
            wave[1] = ((i shr 8) and 0xFF).toByte()
            wave[2] = ((i shr 16) and 0xFF).toByte()
        }
    var y: Int
        get() = (wave.toInt888() shr 5) and 0x3FF
        set(value) {
            if (value < 0 || value > 1023) throw DataOverflowException()
            val i = 0 or ((z and 0x1F) shl 15) or ((value and 0x3FF) shl 5) or (x and 0x1F)
            wave[0] = (i and 0xFF).toByte()
            wave[1] = ((i shr 8) and 0xFF).toByte()
            wave[2] = ((i shr 16) and 0xFF).toByte()
        }
    var z: Int
        get() = (wave.toInt888() shr 15) and 0x1F
        set(value) {
            if (value < 0 || value > 31) throw DataOverflowException()
            val i = 0 or ((value and 0x1F) shl 15) or ((y and 0x3FF) shl 5) or (x and 0x1F)
            wave[0] = (i and 0xFF).toByte()
            wave[1] = ((i shr 8) and 0xFF).toByte()
            wave[2] = ((i shr 16) and 0xFF).toByte()
        }


    fun getXYZ() = Triple(x, y, z)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as WaveData

        if (!wave.contentEquals(other.wave)) return false

        return true
    }

    override fun hashCode(): Int {
        return wave.contentHashCode()
    }
}