package com.chsteam.mypets.internal.compatibility.dungeonlab

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chsteam.mypets.internal.bluetooth.BluetoothViewModel
import com.chsteam.mypets.internal.compatibility.Device
import com.chsteam.mypets.internal.compatibility.Devices
import com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.DGLabBLEDevice
import com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.OpenDGLab
import com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.data.AutoWaveData
import com.chsteam.mypets.internal.compatibility.dungeonlab.opendglab.data.WaveData
import com.chsteam.mypets.internal.schedule.CoroutineTimer
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.callback.BleReadCallback
import com.clj.fastble.callback.BleWriteCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import kotlinx.coroutines.DelicateCoroutinesApi
import java.lang.Math.random


@OptIn(DelicateCoroutinesApi::class)
@SuppressLint("MissingPermission")
class DungeonLabV2(context: Context, viewModel: BluetoothViewModel, bleDevice: BleDevice) : Device(context, viewModel, bleDevice) {

    companion object {
        private const val BATTERY = "955A180A-0FE2-F5AA-A094-84B8D4F3E8AD"
        private const val BATTERY_NOTIFY = "955A1500-0FE2-F5AA-A094-84B8D4F3E8AD"
        private const val FUNCTION = "955A180B-0FE2-F5AA-A094-84B8D4F3E8AD"
        private const val STRENGTH = "955A1504-0FE2-F5AA-A094-84B8D4F3E8AD"
        private const val A_WAVE = "955A1505-0FE2-F5AA-A094-84B8D4F3E8AD"
        private const val B_WAVE = "955A1506-0FE2-F5AA-A094-84B8D4F3E8AD"
    }

    override val type: Devices
        get() = Devices.DUNGEON_LAB_V2

    override val tickRate: Int
        get() = 10


    val channelA = mutableStateOf(0)
    val channelB = mutableStateOf(0)

    val activeChannelAWave = mutableStateOf(AutoWaveData.AutoWaveType.OFF)
    val activeChannelBWave = mutableStateOf(AutoWaveData.AutoWaveType.OFF)

    val enableChanelAWave = mutableStateOf(listOf<AutoWaveData.AutoWaveType>())
    val enableChanelBWave = mutableStateOf(listOf<AutoWaveData.AutoWaveType>())

    val device = DGLabBLEDevice(::waveSender, ::powerSender, ::powerCallback, ::batteryCallback)

    private val showChannelA =  mutableStateOf(true)

    init {
        setupWaveSelection()
    }

    override fun tick() {
        device.callAutoWaveTimer()
    }

    @Composable
    override fun DeviceCardContent() {
        DeviceCard(channelAStrength = channelA.value.toFloat(), channelBStrength = channelB.value.toFloat())
    }

    @Composable
    fun DeviceCard(
        channelAStrength: Float,
        channelBStrength: Float,
    ) {
        ChannelControl("A通道", channelAStrength)
        Spacer(Modifier.height(8.dp))
        ChannelControl("B通道", channelBStrength)
        Spacer(Modifier.height(8.dp))
    }

    @Composable
    override fun CardClickedShow( onDismissRequest: () -> Unit) {
        Dialog(onDismissRequest = { onDismissRequest() }) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                ChannelControlUI()
            }
        }
    }

    @Composable
    fun ChannelControlUI() {
        // 通道控制UI
        ChannelControl("A通道", channelA.value.toFloat())
        Spacer(Modifier.height(8.dp))
        ChannelControl("B通道", channelB.value.toFloat())
        Spacer(Modifier.height(8.dp))

        // 通道切换按钮
        ChannelSwitchButtons()

        // 根据当前显示的通道，显示对应的波形选择器
        WaveformSelectorUI()
    }

    @Composable
    fun WaveformSelectorUI() {
        if (showChannelA.value) {
            WaveformSelectorComponent(activeChannelWave = activeChannelAWave, enableChannelWave = enableChanelAWave)
        } else {
            WaveformSelectorComponent(activeChannelWave = activeChannelBWave, enableChannelWave = enableChanelBWave)
        }
        Spacer(Modifier.height(8.dp))
    }

    @Composable
    fun WaveformSelectorComponent(activeChannelWave: MutableState<AutoWaveData.AutoWaveType>, enableChannelWave: MutableState<List<AutoWaveData.AutoWaveType>>) {
        WaveformSelector(
            activeChannelWave = activeChannelWave,
            enableChanelWave = enableChannelWave,
        ) { type ->
            if (type in enableChannelWave.value) {
                enableChannelWave.value = enableChannelWave.value - type
            } else {
                enableChannelWave.value = enableChannelWave.value + type
            }

            if(activeChannelWave.value == type) {
                if(enableChannelWave.value.isEmpty()) {
                    activeChannelWave.value = AutoWaveData.AutoWaveType.OFF
                } else {
                    activeChannelWave.value = enableChannelWave.value.random()
                }
            } else {
                activeChannelWave.value= type
            }
            this.device.selectAutoWave(this.activeChannelAWave.value.data, this.activeChannelBWave.value.data)
        }
    }

    @Composable
    fun ChannelSwitchButtons() {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = { showChannelA.value = true }, Modifier.padding(end = 8.dp)) {
                Text("A通道")
            }
            Button(onClick = { showChannelA.value = false }, Modifier.padding(start = 8.dp)) {
                Text("B通道")
            }
        }
    }

    @Composable
    fun WaveformSelector(
        activeChannelWave: State<AutoWaveData.AutoWaveType>,
        enableChanelWave: State<List<AutoWaveData.AutoWaveType>>,
        onWaveSelected: (AutoWaveData.AutoWaveType) -> Unit
    ) {
        val waves = AutoWaveData.AutoWaveType.values()
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            items(waves.size - 1) { index ->
                val wave = waves[index + 1]
                val isActive = activeChannelWave.value == wave
                val isEnabled = enableChanelWave.value.any { it== wave }

                val buttonColors = ButtonDefaults.buttonColors(
                    containerColor = when {
                        isActive-> Color.Green
                        isEnabled -> Color.Yellow
                        else -> Color.Red
                    },
                    contentColor = Color.White, // 这里假设按钮文本/图标总是白色
                    disabledContainerColor = Color.Gray, // 禁用状态下的容器颜色
                    disabledContentColor = Color.DarkGray // 禁用状态下的内容颜色
                )

                Button(
                    onClick = { onWaveSelected(wave) },
                    colors = buttonColors,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(wave.waveName)
                }
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
                if(locked) return@Slider
                if(channelName.contains("A")) {
                    channelA.value = fl.toInt()
                    device.channelAPower = fl.toInt()
                    device.selectPower()
                } else {
                    channelB.value = fl.toInt()
                    device.channelBPower = fl.toInt()
                    device.selectPower()
                }
            },
            valueRange = 0f..2047f
        )
        Row {
            Spacer(Modifier.width(8.dp))
        }
    }

    private fun waveSender(a: WaveData, b: WaveData) {
        BleManager.getInstance().write(
            bleDevice,
            FUNCTION,
            A_WAVE,
            a.wave,
            object : BleWriteCallback() {
                override fun onWriteSuccess(current: Int, total: Int, justWrite: ByteArray) {}
                override fun onWriteFailure(exception: BleException) {}
            })
        BleManager.getInstance().write(
            bleDevice,
            FUNCTION,
            B_WAVE,
            b.wave,
            object : BleWriteCallback() {
                override fun onWriteSuccess(current: Int, total: Int, justWrite: ByteArray) {}
                override fun onWriteFailure(exception: BleException) {}
            })
    }

    private fun powerSender(power: ByteArray) {
        BleManager.getInstance().write(
            bleDevice,
            FUNCTION,
            STRENGTH,
            power,
            object : BleWriteCallback() {
                override fun onWriteSuccess(current: Int, total: Int, justWrite: ByteArray) {}
                override fun onWriteFailure(exception: BleException) {}
            })
    }

    private fun powerCallback(a: Int, b: Int) {
        this.channelA.value = a
        this.channelB.value = b
    }

    private fun batteryCallback(level: Int) {
        battery.value = level
    }

    override fun onConnectSuccess(bleDevice: BleDevice, gatt: BluetoothGatt, status: Int) {
        BleManager.getInstance().notify(
            bleDevice,
            BATTERY,
            BATTERY_NOTIFY,
            object : BleNotifyCallback() {
                override fun onNotifySuccess() {}
                override fun onNotifyFailure(exception: BleException) {}
                override fun onCharacteristicChanged(data: ByteArray) {
                    device.callbackBattery(data)
                }
            })
        BleManager.getInstance().read(
            bleDevice,
            BATTERY,
            BATTERY_NOTIFY,
            object : BleReadCallback() {
                override fun onReadSuccess(data: ByteArray) {
                    device.callbackBattery(data)
                }
                override fun onReadFailure(exception: BleException) {}
            })
        BleManager.getInstance().read(
            bleDevice,
            FUNCTION,
            STRENGTH,
            object : BleReadCallback() {
                override fun onReadSuccess(data: ByteArray) {
                    device.callbackPower(data)
                }
                override fun onReadFailure(exception: BleException) {}
            })
    }

    private fun setupWaveSelection() {
        randomSelectWave(OpenDGLab.DGChannel.CHANNEL_A, activeChannelAWave, enableChanelAWave)
        randomSelectWave(OpenDGLab.DGChannel.CHANNEL_B, activeChannelBWave, enableChanelBWave)
    }

    private fun randomSelectWave(
        channel: OpenDGLab.DGChannel,
        activeWave: MutableState<AutoWaveData.AutoWaveType>,
        enabledWaves: State<List<AutoWaveData.AutoWaveType>>
    ) {
        CoroutineTimer().runTaskLater((10000L..30000L).random()) {
            val selectedWave = if (enabledWaves.value.isNotEmpty()) {
                enabledWaves.value.random()
            } else {
                AutoWaveData.AutoWaveType.OFF
            }
            activeWave.value = selectedWave
            device.selectAutoWave(channel, selectedWave.data)
            randomSelectWave(channel, activeWave, enabledWaves) // 递归调用以持续更新波形
        }
    }
}