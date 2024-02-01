package com.chsteam.mypets.internal.compatibility

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chsteam.mypets.internal.bluetooth.BluetoothViewModel
import com.clj.fastble.data.BleDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

abstract class Device(val context: Context, val viewModel: BluetoothViewModel, val bleDevice: BleDevice) {

    abstract val type : Devices
    abstract val tickRate: Int

    var deviceName: String = context.getSharedPreferences("MyPetsDeviceName", Context.MODE_PRIVATE).getString(bleDevice.mac, type.i18Name) ?: type.i18Name
    val maxReconnectAttempts = 3
    var currentReconnectAttempts = 0
    val battery = mutableStateOf(0)

    var locked = false
        protected set

    var initialized = false

    abstract fun tick()

    @Composable
    abstract fun DeviceCardContent()

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

    fun changeDeviceName(string: String) {
        context.getSharedPreferences("MyPetsDeviceName", Context.MODE_PRIVATE)
            .edit().putString(this.bleDevice.mac, string).apply()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DeviceCard() {

        var showDialog by remember { mutableStateOf(false) }

        var name = deviceName

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = deviceName,
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.clickable { showDialog = true }
                    )
                    DeviceCardContent()
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("修改设备名") },
                text = {
                    TextField(
                        value = name,
                        onValueChange = { name = it }
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            changeDeviceName(name)
                            showDialog = false
                        }
                    ) {
                        Text("确认")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("取消")
                    }
                }
            )
        }
    }
}