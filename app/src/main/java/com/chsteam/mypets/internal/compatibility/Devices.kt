package com.chsteam.mypets.internal.compatibility

import android.bluetooth.BluetoothDevice
import android.content.Context
import com.chsteam.mypets.internal.bluetooth.BluetoothViewModel
import com.chsteam.mypets.internal.compatibility.dungeonlab.DungeonLabV2
import com.chsteam.mypets.internal.compatibility.dungeonlab.DungeonLabV3

enum class Devices(val i18Name: String,val bluetoothName: String, val BLE: Boolean = true) {
    DUNGEON_LAB_V2("郊狼第二代", "D-LAB ESTIM01"),
    DUNGEON_LAB_V3("郊狼第三代", "D-LAB ESTIM02");

    fun getDevice(context: Context, viewModel: BluetoothViewModel,bluetoothDevice: BluetoothDevice): Device {
        return when(this) {
            DUNGEON_LAB_V2 -> DungeonLabV2(context, viewModel, bluetoothDevice)
            DUNGEON_LAB_V3 -> DungeonLabV3(context, viewModel, bluetoothDevice)
        }
    }
}