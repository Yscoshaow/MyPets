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
import com.chsteam.mypets.internal.compatibility.Devices

class ShockObjective(instruction: Instruction) : Objective(instruction) {
    override val typeName: String
        get() = "电击任务"

    private val text = "你要自己在大腿和小腹上贴上点击贴片，持续电10分钟哦。如果你准备好了的话，就开始吧"

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun TaskCard() {

        var expanded by remember { mutableStateOf(false) }
        var selectedOption by remember { mutableStateOf(Devices.DUNGEON_LAB_V2) }

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

                        viewModel.availabilityDevice.value.filter { it.type == Devices.DUNGEON_LAB_V3 || it.type == Devices.DUNGEON_LAB_V2 }.forEach { device ->
                            DropdownMenuItem(text = { Text(text = device.type.i18Name) }, onClick = {
                                selectedOption = device.type
                                expanded = false
                            })
                        }

                    }
                }
                Spacer(Modifier.height(12.dp))
                Row {

                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Outlined.Start, contentDescription = "开始")
                    }
                }
            }
        }
    }
}