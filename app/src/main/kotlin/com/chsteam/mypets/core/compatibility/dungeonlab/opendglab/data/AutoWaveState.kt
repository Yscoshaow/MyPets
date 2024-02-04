package com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data

import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.AutoWaveStateMachine

data class AutoWaveState(val wave: AutoWaveData, var a: Int, var b: Int, var c: Int, var pc: Int, var j: Int, var points: Array<AutoWavePoint>, var section: Int,
                         var waveTiming: Int = 1, var lastWaveMaxTiming: Int = 1, var waveStrength: Float = 0f, var stateMachine: AutoWaveStateMachine = AutoWaveStateMachine.SEND,
                         var pow: Int = 1, var i7: Int = 0, var i2: Int = 0, var i3: Int = 0, val wavePlot: IntArray = IntArray(100), var f785A: Int = 0, val f813y: IntArray = IntArray(100), var f811w: Int = 0, var f812x: Int = 0) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as AutoWaveState

        if (wave != other.wave) return false
        if (a != other.a) return false
        if (b != other.b) return false
        if (c != other.c) return false
        if (pc != other.pc) return false
        if (j != other.j) return false
        if (!points.contentEquals(other.points)) return false
        if (section != other.section) return false
        if (waveTiming != other.waveTiming) return false
        if (lastWaveMaxTiming != other.lastWaveMaxTiming) return false
        if (waveStrength != other.waveStrength) return false
        if (stateMachine != other.stateMachine) return false
        if (pow != other.pow) return false
        if (i7 != other.i7) return false
        if (i2 != other.i2) return false
        if (i3 != other.i3) return false
        if (!wavePlot.contentEquals(other.wavePlot)) return false
        if (f785A != other.f785A) return false
        if (!f813y.contentEquals(other.f813y)) return false
        if (f811w != other.f811w) return false
        if (f812x != other.f812x) return false

        return true
    }

    override fun hashCode(): Int {
        var result = wave.hashCode()
        result = 31 * result + a
        result = 31 * result + b
        result = 31 * result + c
        result = 31 * result + pc
        result = 31 * result + j
        result = 31 * result + points.contentHashCode()
        result = 31 * result + section
        result = 31 * result + waveTiming
        result = 31 * result + lastWaveMaxTiming
        result = 31 * result + waveStrength.hashCode()
        result = 31 * result + stateMachine.hashCode()
        result = 31 * result + pow
        result = 31 * result + i7
        result = 31 * result + i2
        result = 31 * result + i3
        result = 31 * result + wavePlot.contentHashCode()
        result = 31 * result + f785A
        result = 31 * result + f813y.contentHashCode()
        result = 31 * result + f811w
        result = 31 * result + f812x
        return result
    }
}