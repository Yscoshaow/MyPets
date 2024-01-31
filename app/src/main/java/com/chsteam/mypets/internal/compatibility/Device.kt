package com.chsteam.mypets.internal.compatibility

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import com.chsteam.mypets.internal.bluetooth.BluetoothViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

abstract class Device(val context: Context, val viewModel: BluetoothViewModel, val device: BluetoothDevice) {

    abstract val type : Devices
    abstract val tickRate: Int

    val battery = mutableStateOf(0)

    var locked = false

    var initialized = false

    abstract fun tick()

    @Composable
    abstract fun DeviceCard()

    private var tickerJob: Job? = null

    init {
        startTicker()
    }

    private fun startTicker() {
        tickerJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                delay(1000L / tickRate)
                tick()
            }
        }
    }

    fun stopTicker() {
        tickerJob?.cancel()
    }
}