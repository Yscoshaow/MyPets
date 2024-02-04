package com.chsteam.mypets.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Start
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chsteam.mypets.core.bluetooth.BluetoothViewModel
import com.chsteam.mypets.core.compatibility.ControlType
import com.chsteam.mypets.core.compatibility.Devices
import com.chsteam.mypets.core.compatibility.dungeonlab.DungeonLabV2
import com.chsteam.mypets.core.compatibility.dungeonlab.opendglab.data.AutoWaveData
import com.chsteam.mypets.core.experimental.ExperimentalViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ExperimentalPage : Page, KoinComponent {

    private val viewModel: BluetoothViewModel by inject()

    private val expViewModel: ExperimentalViewModel by inject()

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Main(navController: NavController) {
        val content: @Composable (PaddingValues) -> Unit = { innerPadding ->
            Experimental(innerPadding = innerPadding)
        }

        Scaffold(
            topBar = { TopBar(navController = navController) },
            content = content
        )
    }

    @Composable
    fun Experimental(innerPadding: PaddingValues) {
        val context = LocalContext.current
        Column(modifier = Modifier.padding(innerPadding)) {
            Divider()
            SpeedControllerDGCard()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SpeedControllerDGCard() {

        var expanded by remember { mutableStateOf(false) }

        val context = LocalContext.current
        var dungeonLabV2 = expViewModel.speedController.value

        var selectedOption by remember { if(dungeonLabV2 == null) {
            mutableStateOf("")
        } else {
            mutableStateOf(dungeonLabV2!!.deviceName.value)
        }
        }

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "步速小游戏", style = MaterialTheme.typography.displaySmall)
                Spacer(Modifier.height(12.dp))

                TextField(value = "开启本游戏后，你需要保持步速达到2m/s, 不然就会遭到郊狼电击哦. 你当前的速度是 ${dungeonLabV2?.speed?.value?: 0f}", onValueChange = {}, readOnly = true)
                Spacer(Modifier.height(12.dp))
                Column {
                    TextField(
                        modifier = Modifier.fillMaxWidth(1f),
                        value = selectedOption,
                        onValueChange = {  },
                        label = { Text("设备类型") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text
                        ),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                    DropdownMenu(
                        modifier = Modifier.fillMaxWidth(0.84f),
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        viewModel.availabilityDevice.value.filter { it.type == Devices.DUNGEON_LAB_V2 }.forEach { device ->
                            DropdownMenuItem(text = { Text(text = device.deviceName.value) }, onClick = {
                                selectedOption = device.deviceName.value
                                expanded = false
                                dungeonLabV2 = device as DungeonLabV2
                            })
                        }

                    }
                }
                Spacer(Modifier.height(12.dp))
                Row {
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = {
                        if(dungeonLabV2 != null) {
                            dungeonLabV2?.controlType = ControlType.SENSOR
                            dungeonLabV2?.activeSpeed()
                            dungeonLabV2?.enableChanelAWave?.value =
                                AutoWaveData.AutoWaveType.values().filter { it != AutoWaveData.AutoWaveType.OFF }
                                    .toList()
                            dungeonLabV2?.enableChanelBWave?.value =
                                AutoWaveData.AutoWaveType.values().filter { it != AutoWaveData.AutoWaveType.OFF }
                                    .toList()
                            Toast.makeText(context, "游戏开始", Toast.LENGTH_LONG).show()
                        }
                    }) {
                        Icon(Icons.Outlined.Start, contentDescription = "开始")
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(navController: NavController) {
        TopAppBar(
            title = { Text(text = "Experimental") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
        )
    }
}