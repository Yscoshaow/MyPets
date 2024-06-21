package com.chsteam.mypets.core.compatibility.dungeonlab.mypets

import com.chsteam.mypets.core.compatibility.dungeonlab.DGLabDevice
import com.chsteam.mypets.core.compatibility.dungeonlab.mypets.data.ParseMethod
import com.chsteam.mypets.core.compatibility.dungeonlab.mypets.data.Power
import com.chsteam.mypets.core.compatibility.dungeonlab.mypets.data.WaveData
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.DataOverflowException
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.OpenDGLab
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data.AutoWaveData
import java.nio.ByteBuffer

class DGLabBLEDeviceV3(val dataSender: (ByteArray) -> Unit) : DGLabDevice() {

    fun stopAll() {
        update(0, 0)
        selectAutoWave(OpenDGLab.DGChannel.CHANNEL_A, AutoWaveData.AutoWaveType.OFF.data)
        selectAutoWave(OpenDGLab.DGChannel.CHANNEL_B, AutoWaveData.AutoWaveType.OFF.data)
    }

    override fun selectPower(a: Int, b: Int) {
        // Skip it
    }

    fun stop(channel: OpenDGLab.DGChannel) {
        when(channel) {
            OpenDGLab.DGChannel.CHANNEL_A -> {
                update(a = 0)
                selectAutoWave(OpenDGLab.DGChannel.CHANNEL_A, AutoWaveData.AutoWaveType.OFF.data)
            }
            OpenDGLab.DGChannel.CHANNEL_B -> {
                update(b = 0)
                selectAutoWave(OpenDGLab.DGChannel.CHANNEL_B, AutoWaveData.AutoWaveType.OFF.data)
            }
        }
    }

    // 100ms (0.1s) 调用一次
    fun update(a: Int = channelAPower, b: Int = channelBPower) {
        val (aWave, waveAState) = OpenDGLab.calcAutoWave(channelAWave)
        channelAWave = waveAState

        val (bWave, waveBState) = OpenDGLab.calcAutoWave(channelBWave)
        channelBWave = waveBState
        if (channelAPower == 0 && channelBPower == 0) return
        dataSender(createByteArray(0, parseMethod = ParseMethod.SET, power =  Power(a, b), WaveData.convertFromV2(aWave), WaveData.convertFromV2(bWave)))
    }

    fun dataParas(data: ByteArray) {
        when(data[0].toInt()) {
            0xB1 -> {
                channelAPower = data[2].toInt()
                channelAPower = data[3].toInt()
            }
            0xBE -> {
            }
        }
    }

    private fun createByteArray(
        sequenceNumber: Int = 0,
        parseMethod: ParseMethod = ParseMethod.IGNORE,
        power: Power = Power(0, 0),
        waveDataA: WaveData,
        waveDataB: WaveData
    ): ByteArray {

        if (sequenceNumber < 0 || sequenceNumber > 15) {
            throw DataOverflowException()
        }

        val byteArray = ByteArray(20)
        val buffer = ByteBuffer.wrap(byteArray)
        buffer.put(0xB0.toByte())
        val combinedByte = (sequenceNumber shl 4 or parseMethod.int).toByte()
        buffer.put(combinedByte)
        buffer.put(power.power)
        buffer.put(waveDataA.wave)
        buffer.put(waveDataB.wave)

        return byteArray
    }
}