package com.chsteam.mypets.internal.compatibility

import android.bluetooth.BluetoothGatt
import android.content.Context
import android.os.Handler
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
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

abstract class Device(val context: Context, val viewModel: BluetoothViewModel, val bleDevice: BleDevice) {

    abstract val type : Devices

    abstract val tickRate: Int

    var deviceName = mutableStateOf(context.getSharedPreferences("MyPetsDeviceName", Context.MODE_PRIVATE).getString(bleDevice.mac, type.i18Name) ?: type.i18Name)

    val battery = mutableStateOf(0)

    var locked = false

    private var initialized = false

    private val maxReconnectAttempts = 5

    private var currentReconnectAttempts = 0

    private var tickerJob: Job? = null

    init {
        bleConnect()
        startTicker()
    }

    abstract fun tick()

    @Composable
    abstract fun DeviceCardContent()

    abstract fun onConnectSuccess(bleDevice: BleDevice, gatt: BluetoothGatt, status: Int)

    @Composable
    open fun CardClickedShow() {
        // override it and show a dialog
    }

    open fun onCardClick() {

    }


    open fun onStartConnect() {

    }

    open fun onConnectFail(bleDevice: BleDevice, exception: BleException) {

    }

    open fun onDisConnected() {

    }

    private fun startTicker() {
        tickerJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                if(!initialized) continue
                delay(1000L / tickRate)
                tick()
            }
        }
    }

    fun stopTicker() {
        tickerJob?.cancel()
    }

    private fun changeDeviceName(string: String) {
        deviceName.value = string
        context.getSharedPreferences("MyPetsDeviceName", Context.MODE_PRIVATE)
            .edit().putString(this.bleDevice.mac, string).apply()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DeviceCard() {

        var showDialog by remember { mutableStateOf(false) }
        var showCardClick by remember { mutableStateOf(false) }

        var name by remember { mutableStateOf(deviceName.value) }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clickable {
                    if (locked) return@clickable
                    onCardClick()
                    showCardClick = true
                }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = deviceName.value,
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.clickable { showDialog = true }
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text("电量: ${(battery.value)}%")
                LinearProgressIndicator(progress = battery.value.toFloat())
                Spacer(Modifier.height(8.dp))

                DeviceCardContent()
            }
        }

        if(showCardClick) {
            CardClickedShow()
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

    private fun bleConnect(reconnect: Boolean = false) {
        BleManager.getInstance().connect(bleDevice, object : BleGattCallback() {
            override fun onStartConnect() {
                this@Device.onStartConnect()
            }
            override fun onConnectFail(bleDevice: BleDevice, exception: BleException) {
                this@Device.onConnectFail(bleDevice, exception)
            }
            override fun onConnectSuccess(bleDevice: BleDevice, gatt: BluetoothGatt, status: Int) {
                if(!reconnect) {
                    viewModel.availabilityDevice.value = viewModel.availabilityDevice.value + this@Device
                }
                this@Device.onConnectSuccess(bleDevice, gatt, status)
                this@Device.initialized = true
            }
            override fun onDisConnected(
                isActiveDisConnected: Boolean,
                bleDevice: BleDevice,
                gatt: BluetoothGatt,
                status: Int
            ) {
                this@Device.initialized = false
                handleReconnect()
                this@Device.onDisConnected()
            }
        })
    }

    private fun handleReconnect() {
        if (currentReconnectAttempts < maxReconnectAttempts) {
            currentReconnectAttempts++
            Handler().postDelayed({
                bleConnect(true)
            }, 500)
        } else {
            viewModel.availabilityDevice.value = viewModel.availabilityDevice.value - this
        }
    }
}