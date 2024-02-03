package com.chsteam.mypets.internal.experimental

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chsteam.mypets.internal.compatibility.dungeonlab.DungeonLabV2
import com.clj.fastble.data.BleDevice

class ExperimentalViewModel : ViewModel() {

    val speedController = mutableStateOf<DungeonLabV2?>(null)
}