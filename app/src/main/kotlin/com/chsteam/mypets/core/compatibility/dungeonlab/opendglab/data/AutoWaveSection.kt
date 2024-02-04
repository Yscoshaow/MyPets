package com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data

import kotlin.math.ceil
import kotlin.math.pow

data class AutoWaveSection(val _a: Int, val _b: Int, val c: Int, val _j: Int, val pc: Int, val points: Array<AutoWavePoint>) {
    val a = _a * 20 + 1000
    val b = _b * 20 + 1000
    val j = ceil(((_j + 1).toDouble() / 101.0).pow(1.6) * 100.0).toInt()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AutoWaveSection

        if (_a != other._a) return false
        if (_b != other._b) return false
        if (c != other.c) return false
        if (_j != other._j) return false
        if (pc != other.pc) return false
        if (!points.contentEquals(other.points)) return false
        if (a != other.a) return false
        if (b != other.b) return false
        if (j != other.j) return false

        return true
    }

    override fun hashCode(): Int {
        var result = _a
        result = 31 * result + _b
        result = 31 * result + c
        result = 31 * result + _j
        result = 31 * result + pc
        result = 31 * result + points.contentHashCode()
        result = 31 * result + a
        result = 31 * result + b
        result = 31 * result + j
        return result
    }
}