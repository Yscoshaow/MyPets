package com.chsteam.mypets.core.compatibility.dungeonlab

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.content.Context
import com.chsteam.mypets.core.bluetooth.BluetoothViewModel
import com.chsteam.mypets.core.compatibility.Devices
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.DGLabBLEDeviceV2
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data.WaveData
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.callback.BleReadCallback
import com.clj.fastble.callback.BleWriteCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException


@SuppressLint("MissingPermission")
class DungeonLabV2(context: Context, viewModel: BluetoothViewModel, bleDevice: BleDevice) : DungeonLab(context, viewModel, bleDevice) {

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

    override
    val device = DGLabBLEDeviceV2(::waveSender, ::powerSender, ::powerCallback, ::batteryCallback)

    override fun tick() {
        device.callAutoWaveTimer()
    }

    private fun waveSender(a: WaveData, b: WaveData) {
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
    }
}