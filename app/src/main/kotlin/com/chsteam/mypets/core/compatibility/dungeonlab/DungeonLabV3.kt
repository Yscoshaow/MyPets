package com.chsteam.mypets.core.compatibility.dungeonlab

import android.bluetooth.BluetoothGatt
import android.content.Context
import com.chsteam.mypets.core.bluetooth.BluetoothViewModel
import com.chsteam.mypets.core.compatibility.Devices
import com.chsteam.mypets.core.compatibility.dungeonlab.mypets.DGLabBLEDeviceV3
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.callback.BleReadCallback
import com.clj.fastble.callback.BleWriteCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException

class DungeonLabV3(context: Context, viewModel: BluetoothViewModel, bleDevice: BleDevice) : DungeonLab(context, viewModel, bleDevice) {

    companion object {
        private const val WRITE_SERVICE = "0000180C-0000-1000-8000-00805f9b34fb"
        private const val WRITE = "0000150A-0000-1000-8000-00805f9b34fb"
        private const val NOTIFY_SERVICE = "0000180C-0000-1000-8000-00805f9b34fb"
        private const val NOTIFY = "0000150B-0000-1000-8000-00805f9b34fb"
        private const val BATTERY_SERVICE = "0000180A-0000-1000-8000-00805f9b34fb"
        private const val BATTERY = "00001500-0000-1000-8000-00805f9b34fb"
    }

    override val type: Devices
        get() = Devices.DUNGEON_LAB_V3

    override
    val device = DGLabBLEDeviceV3(::dataSender)

    override fun tick() {
        device.update()
    }

    override fun onConnectSuccess(bleDevice: BleDevice, gatt: BluetoothGatt, status: Int) {
        BleManager.getInstance().read(
            bleDevice,
            BATTERY_SERVICE,
            BATTERY,
            object : BleReadCallback() {
                override fun onReadFailure(exception: BleException) {}
                override fun onReadSuccess(data: ByteArray) {
                    this@DungeonLabV3.battery.value = data[0].toInt()
                }
            }
        )

        BleManager.getInstance().notify(
            bleDevice,
            NOTIFY_SERVICE,
            NOTIFY,
            object : BleNotifyCallback() {
                override fun onNotifySuccess() {}
                override fun onNotifyFailure(exception: BleException) {}
                override fun onCharacteristicChanged(data: ByteArray) {
                   device.dataParas(data)
                }
            }
        )
    }

    private fun dataSender(data: ByteArray) {
        BleManager.getInstance().write(
            bleDevice,
            WRITE_SERVICE,
            WRITE,
            data,
            object : BleWriteCallback() {
                override fun onWriteSuccess(current: Int, total: Int, justWrite: ByteArray) {}
                override fun onWriteFailure(exception: BleException) {}
            })
    }
}