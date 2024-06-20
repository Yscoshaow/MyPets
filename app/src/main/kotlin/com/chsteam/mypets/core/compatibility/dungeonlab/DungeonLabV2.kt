package com.chsteam.mypets.core.compatibility.dungeonlab

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.outlined.DeveloperMode
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chsteam.mypets.core.bluetooth.BluetoothViewModel
import com.chsteam.mypets.core.compatibility.ControlType
import com.chsteam.mypets.core.compatibility.Device
import com.chsteam.mypets.core.compatibility.Devices
import com.chsteam.mypets.core.compatibility.controller.SpeedController
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.DGLabBLEDevice
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.OpenDGLab
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data.AutoWaveData
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data.WaveData
import com.chsteam.mypets.core.schedule.CoroutineTimer
import com.chsteam.mypets.core.utils.calculateExpression
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.callback.BleReadCallback
import com.clj.fastble.callback.BleWriteCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.DelicateCoroutinesApi


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
    val device = DGLabBLEDevice(::waveSender, ::powerSender, ::powerCallback, ::batteryCallback)

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