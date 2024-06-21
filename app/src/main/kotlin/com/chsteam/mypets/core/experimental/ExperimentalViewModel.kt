package com.chsteam.mypets.core.experimental

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chsteam.mypets.core.compatibility.dungeonlab.DungeonLab

class ExperimentalViewModel : ViewModel() {

    val speedController = mutableStateOf<DungeonLab?>(null)
}