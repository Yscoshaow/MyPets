package com.chsteam.mypets.internal.objectives

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Start
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chsteam.mypets.api.Objective
import com.chsteam.mypets.internal.Instruction
import com.chsteam.mypets.internal.bluetooth.BluetoothViewModel
import com.chsteam.mypets.internal.compatibility.Device
import com.chsteam.mypets.internal.compatibility.Devices

class ShockObjective(instruction: Instruction) : Objective(instruction) {

    companion object {
        private val availableDevices = listOf(Devices.DUNGEON_LAB_V3, Devices.DUNGEON_LAB_V2)
    }

    override val typeName: String
        get() = "电击任务"

    private val text: String

    private var deivece: Device? = null

    init {
        text = instruction.next() ?: ""
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun TaskCard() {

        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf("") }

        val viewModel: BluetoothViewModel = viewModel()

        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = typeName, style = MaterialTheme.typography.displaySmall)
                Spacer(Modifier.height(12.dp))
                TextField(value = text, onValueChange = {}, readOnly = true)
                Spacer(Modifier.height(12.dp))
                Column {
                    TextField(
                        modifier = Modifier.fillMaxWidth(1f),
                        value = "设备选择",
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

                        viewModel.availabilityDevice.value.filter { it.type in availableDevices }.forEach { device ->
                            DropdownMenuItem(text = { Text(text = device.deviceName.value) }, onClick = {
                                selectedOption = device.deviceName.value
                                expanded = false
                                this@ShockObjective.deivece = device
                            })
                        }

                    }
                }
                Spacer(Modifier.height(12.dp))
                Row {

                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = {
                        deivece?.locked
                    }) {
                        Icon(Icons.Outlined.Start, contentDescription = "开始")
                    }
                }
            }
        }
    }
}