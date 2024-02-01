package com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab

import com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.data.AutoWaveData
import com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.data.AutoWaveState
import com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.data.BatteryLevel
import com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.data.Power
import com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.data.WaveData

class DGLabBLEDevice(val waveSender: (a: WaveData, b: WaveData) -> Unit, val powerSender: (power: ByteArray) -> Unit, val powerCallback: (a: Int, b: Int) -> Unit, val batteryCallback: (level: Int) -> Unit) {

    var channelAPower = 0
    var channelBPower = 0

    private lateinit var channelAWave: AutoWaveState
    private lateinit var channelBWave: AutoWaveState

    init {
        selectAutoWave(OpenDGLab.DGChannel.CHANNEL_A, AutoWaveData.AutoWaveType.BREATH.data)
        selectAutoWave(OpenDGLab.DGChannel.CHANNEL_B, AutoWaveData.AutoWaveType.BREATH.data)
    }

    fun selectAutoWave(channel: OpenDGLab.DGChannel, wave: AutoWaveData) {
        when (channel) {
            OpenDGLab.DGChannel.CHANNEL_A -> {
                channelAWave = AutoWaveState(wave=wave, wave.sections[0].a,wave.sections[0].b,wave.sections[0].c,wave.sections[0].pc,wave.sections[0].j, wave.sections[0].points, 0)
            }
            OpenDGLab.DGChannel.CHANNEL_B -> {
                channelBWave = AutoWaveState(wave=wave, wave.sections[0].a,wave.sections[0].b,wave.sections[0].c,wave.sections[0].pc,wave.sections[0].j, wave.sections[0].points, 0)
            }
        }
    }

    fun selectPower(a: Int = channelAPower, b: Int = channelBPower) {
        if (a < 0 || a > 2047) throw DataOverflowException()
        if (b < 0 || b > 2047) throw DataOverflowException()
        // 郊狼 v2 是反的
        powerSender(Power(b, a).power)
    }

    fun stopAll() {
        powerSender(Power(0, 0).power)
        selectAutoWave(OpenDGLab.DGChannel.CHANNEL_A, AutoWaveData.AutoWaveType.OFF.data)
        selectAutoWave(OpenDGLab.DGChannel.CHANNEL_B, AutoWaveData.AutoWaveType.OFF.data)
    }

    fun stop(channel: OpenDGLab.DGChannel) {
        when(channel) {
            OpenDGLab.DGChannel.CHANNEL_A -> {
                powerSender(Power(0, channelBPower).power)
                selectAutoWave(OpenDGLab.DGChannel.CHANNEL_A, AutoWaveData.AutoWaveType.OFF.data)
            }
            OpenDGLab.DGChannel.CHANNEL_B -> {
                powerSender(Power(channelAPower, 0).power)
                selectAutoWave(OpenDGLab.DGChannel.CHANNEL_B, AutoWaveData.AutoWaveType.OFF.data)
            }
        }
    }

    // 100ms (0.1s) 调用一次
    fun callAutoWaveTimer() {
        val (aWave, waveAState) = OpenDGLab.calcAutoWave(channelAWave)
        channelAWave = waveAState

        val (bWave, waveBState) = OpenDGLab.calcAutoWave(channelBWave)
        channelBWave = waveBState
        if (channelAPower == 0 && channelBPower == 0) return
        waveSender(aWave, bWave)
    }

    fun callbackBattery(level: ByteArray) {
        batteryCallback(BatteryLevel(level).getLevel())
    }

    fun callbackPower(power: ByteArray) {
        val sPower = Power(power)
        // 郊狼 v2 是反的
        channelAPower = sPower.b
        channelBPower = sPower.a
        powerCallback(sPower.a, sPower.b)
    }
}