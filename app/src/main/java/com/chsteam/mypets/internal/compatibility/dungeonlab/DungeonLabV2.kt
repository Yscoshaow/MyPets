package com.chsteam.mypets.internal.compatibility.dungeonlab

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.content.Context
import android.os.Handler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chsteam.mypets.internal.bluetooth.BluetoothViewModel
import com.chsteam.mypets.internal.compatibility.Device
import com.chsteam.mypets.internal.compatibility.Devices
import com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.DGLabBLEDevice
import com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.DGLabStruct
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.callback.BleReadCallback
import com.clj.fastble.callback.BleWriteCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import kotlinx.coroutines.DelicateCoroutinesApi


@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("MissingPermission")
class DungeonLabV2(context: Context, viewModel: BluetoothViewModel, bleDevice: BleDevice) : Device(context, viewModel, bleDevice) {

    companion object {
        private const val BATTERY = "955A180A-0FE2-F5AA-A094-84B8D4F3E8AD"
        private const val BATTERY_NOTIFY = "955A1500-0FE2-F5AA-A094-84B8D4F3E8AD"
        private const val FUNCTION = "955A180B-0FE2-F5AA-A094-84B8D4F3E8AD"
        private const val STRENGTH = "955A1504-0FE2-F5AA-A094-84B8D4F3E8AD"
        private const val A_WAVE = "955A1505-0FE2-F5AA-A094-84B8D4F3E8AD"
        private const val B_WAVE = "955A1506-0FE2-F5AA-A094-84B8D4F3E8AD"
    }

    override val type: Devices
        get() = Devices.DUNGEON_LAB_V2

    override val tickRate: Int
        get() = 10


    val channelA = mutableStateOf(0)
    val channelB = mutableStateOf(0)

    val device = DGLabBLEDevice(::waveSender, ::powerSender, ::powerCallback, ::batteryCallback)

    override fun tick() {
        device.callAutoWaveTimer()
    }

    @Composable
    override fun DeviceCardContent() {
        DeviceCard(batteryLevel = battery.value.toFloat() / 100, channelAStrength = channelA.value.toFloat(), channelBStrength = channelB.value.toFloat())
    }

    @Composable
    fun DeviceCard(
        batteryLevel: Float,
        channelAStrength: Float,
        channelBStrength: Float,
    ) {
        Spacer(Modifier.height(8.dp))
        Text("电量: ${(batteryLevel * 100).toInt()}%")
        LinearProgressIndicator(progress = batteryLevel)
        Spacer(Modifier.height(8.dp))

        ChannelControl("A通道", channelAStrength)
        Spacer(Modifier.height(8.dp))
        ChannelControl("B通道", channelBStrength)
        Spacer(Modifier.height(8.dp))
    }

    @Composable
    fun ChannelControl(
        channelName: String,
        strength: Float,
    ) {
        Text(text = "$channelName 强度: ${strength.toInt()}")
        Slider(
            value = strength,
            onValueChange = { fl ->
                if(locked) return@Slider
                if(channelName.contains("A")) {
                    channelA.value = fl.toInt()
                    device.channelAPower = fl.toInt()
                    device.selectPower()
                } else {
                    channelB.value = fl.toInt()
                    device.channelBPower = fl.toInt()
                    device.selectPower()
                }
            },
            valueRange = 0f..2047f
        )
        Row {
            Spacer(Modifier.width(8.dp))
        }
    }

    private fun waveSender(a: DGLabStruct.WaveData, b: DGLabStruct.WaveData) {
        BleManager.getInstance().write(
            bleDevice,
            FUNCTION,
            A_WAVE,
            a.wave,
            object : BleWriteCallback() {
                override fun onWriteSuccess(current: Int, total: Int, justWrite: ByteArray) {}
                override fun onWriteFailure(exception: BleException) {}
            })
        BleManager.getInstance().write(
            bleDevice,
            FUNCTION,
            B_WAVE,
            b.wave,
            object : BleWriteCallback() {
                override fun onWriteSuccess(current: Int, total: Int, justWrite: ByteArray) {}
                override fun onWriteFailure(exception: BleException) {}
            })
    }

    private fun powerSender(power: ByteArray) {
        BleManager.getInstance().write(
            bleDevice,
            FUNCTION,
            STRENGTH,
            power,
            object : BleWriteCallback() {
                override fun onWriteSuccess(current: Int, total: Int, justWrite: ByteArray) {}
                override fun onWriteFailure(exception: BleException) {}
            })
    }

    private fun powerCallback(a: Int, b: Int) {
        this.channelA.value = a
        this.channelB.value = b
    }

    private fun batteryCallback(level: Int) {
        battery.value = level
    }

    override fun onConnectSuccess(bleDevice: BleDevice, gatt: BluetoothGatt, status: Int) {
        super.onConnectSuccess(bleDevice, gatt, status)
        BleManager.getInstance().notify(
            bleDevice,
            BATTERY,
            BATTERY_NOTIFY,
            object : BleNotifyCallback() {
                override fun onNotifySuccess() {}
                override fun onNotifyFailure(exception: BleException) {}
                override fun onCharacteristicChanged(data: ByteArray) {
                    device.callbackBattery(data)
                }
            })
        BleManager.getInstance().read(
            bleDevice,
            BATTERY,
            BATTERY_NOTIFY,
            object : BleReadCallback() {
                override fun onReadSuccess(data: ByteArray) {
                    device.callbackBattery(data)
                }
                override fun onReadFailure(exception: BleException) {}
            })
        BleManager.getInstance().read(
            bleDevice,
            FUNCTION,
            STRENGTH,
            object : BleReadCallback() {
                override fun onReadSuccess(data: ByteArray) {
                    device.callbackPower(data)
                }
                override fun onReadFailure(exception: BleException) {}
            })
    }
}