package com.chsteam.mypets.internal.compatibility.dungeonlab

import android.bluetooth.BluetoothDevice
import android.content.Context
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import com.chsteam.mypets.internal.bluetooth.BluetoothViewModel
import com.chsteam.mypets.internal.compatibility.Device
import com.chsteam.mypets.internal.compatibility.Devices

class DungeonLabV3(context: Context, viewModel: BluetoothViewModel, device: BluetoothDevice) : Device(context, viewModel, device) {

    override val type: Devices
        get() = Devices.DUNGEON_LAB_V3

    override val tickRate: Int
        get() = 1000

    override fun tick() {
        TODO("Not yet implemented")
    }

    @Composable
    override fun DeviceCard() {
        Card {

        }
    }
}