package com.chsteam.mypets.internal.compatibility.dungeonlab

import android.content.Context
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import com.chsteam.mypets.internal.bluetooth.BluetoothViewModel
import com.chsteam.mypets.internal.compatibility.Device
import com.chsteam.mypets.internal.compatibility.Devices
import com.clj.fastble.data.BleDevice

class DungeonLabV3(context: Context, viewModel: BluetoothViewModel, bleDevice: BleDevice) : Device(context, viewModel, bleDevice) {

    override val type: Devices
        get() = Devices.DUNGEON_LAB_V3

    override val tickRate: Int
        get() = 1000

    override fun tick() {
        TODO("Not yet implemented")
    }

    @Composable
    override fun DeviceCardContent() {
        Card {

        }
    }
}