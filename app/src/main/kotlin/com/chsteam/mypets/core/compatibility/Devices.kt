package com.chsteam.mypets.core.compatibility

import android.content.Context
import com.chsteam.mypets.core.bluetooth.BluetoothViewModel
import com.chsteam.mypets.core.compatibility.dungeonlab.DungeonLabV2
import com.chsteam.mypets.core.compatibility.dungeonlab.DungeonLabV3
import com.clj.fastble.data.BleDevice

enum class Devices(val i18Name: String,val bluetoothName: String) {
    DUNGEON_LAB_V2("郊狼第二代", "D-LAB ESTIM01"),
    DUNGEON_LAB_V3("郊狼第三代", "47L121000");

    fun getDevice(context: Context, viewModel: BluetoothViewModel, bleDevice: BleDevice): Device {
        return when(this) {
            DUNGEON_LAB_V2 -> DungeonLabV2(context, viewModel, bleDevice)
            DUNGEON_LAB_V3 -> DungeonLabV3(context, viewModel, bleDevice)
        }
    }
}