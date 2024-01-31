package com.chsteam.mypets.internal.compatibility.dungeonlab

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.chsteam.mypets.MainActivity
import com.chsteam.mypets.internal.bluetooth.BluetoothViewModel
import com.chsteam.mypets.internal.compatibility.Device
import com.chsteam.mypets.internal.compatibility.Devices
import com.sscl.bluetoothlowenergylibrary.BleManager
import com.sscl.bluetoothlowenergylibrary.connetor.multi.BleMultiConnector
import com.sscl.bluetoothlowenergylibrary.intefaces.OnBleConnectStateChangedListener
import com.sscl.bluetoothlowenergylibrary.intefaces.OnCharacteristicReadDataListener
import com.sscl.bluetoothlowenergylibrary.intefaces.OnCharacteristicWriteDataListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.UUID

@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("MissingPermission")
class DungeonLabV2(context: Context, viewModel: BluetoothViewModel, device: BluetoothDevice) : Device(context, viewModel, device) {

    companion object {
        private val BATTERY = UUID.fromString("955A180A-0FE2-F5AA-A094-84B8D4F3E8AD")
        private val BATTERY_READ = UUID.fromString("955A1500-0FE2-F5AA-A094-84B8D4F3E8AD")
        private val FUNCTION = UUID.fromString("955A180B-0FE2-F5AA-A094-84B8D4F3E8AD")
        private val STRENGTH = UUID.fromString("955A1504-0FE2-F5AA-A094-84B8D4F3E8AD")
        private val A_WAVE = UUID.fromString("955A1505-0FE2-F5AA-A094-84B8D4F3E8AD")
        private val B_WAVE = UUID.fromString("955A1506-0FE2-F5AA-A094-84B8D4F3E8AD")
    }

    override val type: Devices
        get() = Devices.DUNGEON_LAB_V2

    override val tickRate: Int
        get() = 10

    private val address = device.address

    private val bleMultiConnector: BleMultiConnector = BleManager.getBleMultiConnector()

    val channelA = mutableStateOf(0)
    val channelB = mutableStateOf(0)

    val az = mutableStateOf(0)
    val ay = mutableStateOf(0)
    val ax = mutableStateOf(0)

    val bz = mutableStateOf(0)
    val by = mutableStateOf(0)
    val bx = mutableStateOf(0)

    init {
        GlobalScope.launch(Dispatchers.IO) {
            bleMultiConnector.connect(device, StateChangedListener(this@DungeonLabV2, viewModel), true, null, null)
        }
    }

    @Composable
    override fun DeviceCard() {
        DeviceCard(deviceName = "Dungeon Lab", batteryLevel = battery.value.toFloat() / 100, channelAStrength = channelA.value.toFloat(), channelBStrength = channelB.value.toFloat())
    }

    @Composable
    fun DeviceCard(
        deviceName: String,
        batteryLevel: Float,
        channelAStrength: Float,
        channelBStrength: Float,
    ) {
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
                    Text(text = deviceName, style = MaterialTheme.typography.displayMedium)
                    //Image(bitmap = avatarImage, contentDescription = "Avatar")
                }
                Spacer(Modifier.height(8.dp))
                Text("电量: ${(batteryLevel * 100).toInt()}%")
                LinearProgressIndicator(progress = batteryLevel)
                Spacer(Modifier.height(8.dp))

                ChannelControl("A通道", channelAStrength)
                Spacer(Modifier.height(8.dp))
                ChannelControl("B通道", channelBStrength)
                Spacer(Modifier.height(8.dp))
            }
        }
    }

    @Composable
    fun ChannelControl(
        channelName: String,
        strength: Float,
    ) {
        Text(text = "$channelName 强度: ${strength.toInt()}")
        Slider(
            value = strength,
            onValueChange = { fl ->
                if(channelName.contains("A")) {
                    channelA.value = fl.toInt()
                } else {
                    channelB.value = fl.toInt()
                }
            },
            valueRange = 0f..2047f
        )
        Row {
            Spacer(Modifier.width(8.dp))
        }
    }

    override fun tick() {
        if(!initialized) return
        val strength = this.compileStrengthToByteArray()
        val channelA = this.compileWaveToByteArray((0..31).random(),(0..1023).random(),(0..31).random())
        val channelB = this.compileWaveToByteArray((0..31).random(),(0..1023).random(),(0..31).random())

        bleMultiConnector.writeCharacteristicData(this.device.address, FUNCTION.toString(), STRENGTH.toString(), strength)
        bleMultiConnector.writeCharacteristicData(this.device.address, FUNCTION.toString(), A_WAVE.toString(), channelA)
        bleMultiConnector.writeCharacteristicData(this.device.address, FUNCTION.toString(), B_WAVE.toString(), channelB)
    }

    private fun initBleConnector() {
        bleMultiConnector.setOnCharacteristicReadDataListener(
            address,
            ReadDataListener(this)
        )
        bleMultiConnector.setOnCharacteristicWriteDataListener(
            address,
            WriteDataListener(this)
        )
    }

    class StateChangedListener(private val dungeonLabV2: DungeonLabV2, private val viewModel: BluetoothViewModel) : OnBleConnectStateChangedListener {
        override fun autoDiscoverServicesFailed() {

        }

        override fun connectTimeout() {
            dungeonLabV2.showToast("Connection to DungeonLab Time out")
        }

        override fun gattStatusError(gattErrorCode: Int) {

        }

        override fun onConnected() {
            dungeonLabV2.initBleConnector()
            viewModel.availabilityDevice.value = viewModel.availabilityDevice.value + dungeonLabV2
            dungeonLabV2.showToast("Dungeon Lab Connect!")
        }

        override fun onDisconnected() {
            viewModel.availabilityDevice.value = viewModel.availabilityDevice.value - dungeonLabV2
            dungeonLabV2.showToast("Dungeon Lab Disconnected!")
        }

        override fun onServicesDiscovered() {
            dungeonLabV2.bleMultiConnector.readCharacteristicData(dungeonLabV2.device.address, BATTERY.toString(), BATTERY_READ.toString())
            dungeonLabV2.bleMultiConnector.readCharacteristicData(dungeonLabV2.device.address, FUNCTION.toString(), STRENGTH.toString())
            dungeonLabV2.bleMultiConnector.readCharacteristicData(dungeonLabV2.device.address, FUNCTION.toString(), A_WAVE.toString())
            dungeonLabV2.bleMultiConnector.readCharacteristicData(dungeonLabV2.device.address, FUNCTION.toString(), B_WAVE.toString())
            dungeonLabV2.initialized = true
        }

        override fun unknownConnectStatus(statusCode: Int) {

        }

    }

    class ReadDataListener(private val dungeonLabV2: DungeonLabV2) : OnCharacteristicReadDataListener {
        override fun onCharacteristicReadData(
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            if(characteristic.service.uuid == BATTERY) {
                dungeonLabV2.battery.value = value[0].toInt()
            } else if(characteristic.service.uuid == FUNCTION) {
                when(characteristic.uuid) {
                    STRENGTH -> {
                        val pwmAB2Data = ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).int and 0xFFFFFF
                        dungeonLabV2.channelB.value = (pwmAB2Data shr 11) and 0x7FF
                        dungeonLabV2.channelA.value = pwmAB2Data and 0x7FF
                    }
                    A_WAVE -> {
                        val pwmA34Data = ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).int and 0xFFFFFF
                        dungeonLabV2.az.value = (pwmA34Data shr 15) and 0x1F
                        dungeonLabV2.ay.value = (pwmA34Data shr 5) and 0x3FF
                        dungeonLabV2.ax.value = pwmA34Data and 0x1F
                    }
                    B_WAVE -> {
                        val pwmB34Data = ByteBuffer.wrap(value).order(ByteOrder.BIG_ENDIAN).int and 0xFFFFFF
                        dungeonLabV2.bz.value = (pwmB34Data shr 15) and 0x1F
                        dungeonLabV2.by.value = (pwmB34Data shr 5) and 0x3FF
                        dungeonLabV2.bx.value = pwmB34Data and 0x1F
                    }
                }
            }
        }
    }

    class WriteDataListener(private val dungeonLabV2: DungeonLabV2) : OnCharacteristicWriteDataListener {
        override fun onCharacteristicWriteData(
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
        }

    }

    private fun showToast(message: String) {
        val context = MainActivity.INSTANCE.applicationContext
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun compileStrengthToByteArray(channelA: Int = this.channelA.value, channelB: Int = this.channelB.value): ByteArray {
        val combined = (channelB shl 11) or channelA
        return byteArrayOf(
            (combined shr 16).toByte(),
            (combined shr 8).toByte(),
            combined.toByte()
        )
    }

    private fun compileWaveToByteArray(ax: Int, ay: Int, az: Int): ByteArray {
        val combined = (az shl 15) or (ay shl 5) or ax
        return byteArrayOf(
            (combined shr 16).toByte(),
            (combined shr 8).toByte(),
            combined.toByte()
        )
    }

    private fun compileAWaveToByteArray(): ByteArray {
        return compileWaveToByteArray(this.ax.value, this.ay.value, this.az.value)
    }

    private fun compileBWaveToByteArray(): ByteArray {
        return compileWaveToByteArray(this.bx.value, this.by.value, this.bz.value)
    }

}