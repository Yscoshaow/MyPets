package com.chsteam.mypets.core.compatibility.dungeonlab

import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.OpenDGLab
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data.AutoWaveData
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data.AutoWaveState

abstract class DGLabDevice {

    var channelAPower = 0
    var channelBPower = 0

    protected lateinit var channelAWave: AutoWaveState
    protected lateinit var channelBWave: AutoWaveState

    init {
        selectAutoWave(OpenDGLab.DGChannel.CHANNEL_A, AutoWaveData.AutoWaveType.BREATH.data)
        selectAutoWave(OpenDGLab.DGChannel.CHANNEL_B, AutoWaveData.AutoWaveType.BREATH.data)
    }

    abstract fun selectPower(a: Int = channelAPower, b: Int = channelBPower);

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

    fun selectAutoWave(waveA: AutoWaveData, waveB: AutoWaveData) {
        channelAWave = AutoWaveState(wave=waveA, waveA.sections[0].a,waveA.sections[0].b,waveA.sections[0].c,waveA.sections[0].pc,waveA.sections[0].j, waveA.sections[0].points, 0)
        channelBWave = AutoWaveState(wave=waveB, waveB.sections[0].a,waveB.sections[0].b,waveB.sections[0].c,waveB.sections[0].pc,waveB.sections[0].j, waveB.sections[0].points, 0)
    }


}