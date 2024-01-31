package com.chsteam.mypets.pages

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chsteam.mypets.internal.bluetooth.BluetoothViewModel
import com.chsteam.mypets.internal.compatibility.Devices
import com.chsteam.mypets.internal.compatibility.dungeonlab.DungeonLabV2
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DevicesPage : Page {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Main(navController: NavController) {

        val viewModel: BluetoothViewModel = viewModel()
        val availabilityDevice = viewModel.availabilityDevice.value
        val showDialog = remember { mutableStateOf(false) }

        val scope = rememberCoroutineScope()
        Scaffold(
            topBar = { TopBar(navController = navController) },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Add Device") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                    onClick = {
                        showDialog.value = true
                    }
                )
            }
        ) { paddingValues ->
            Surface(modifier = Modifier.padding(paddingValues)) {
                LazyColumn {
                    item {
                        availabilityDevice.forEach {
                            it.DeviceCard()
                        }
                    }
                }
            }
        }


        if (showDialog.value) {
            viewModel.initBleScanner()
            AddDeviceDialog(
                viewModel = viewModel,
                scope,
                showDialog,
                onDismiss = {
                    showDialog.value = false
                    viewModel.endBleScanner()
                }
            )
        }
    }

    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AddDeviceDialog(viewModel: BluetoothViewModel, scope: CoroutineScope, show: MutableState<Boolean>, onDismiss: () -> Unit) {
        val availabilityDevice = viewModel.availabilityDevice.value
        val devices = viewModel.bluetoothDevices.value.values
        val context = LocalContext.current
        var selectedOption by remember { mutableStateOf(Devices.DUNGEON_LAB_V2) }
        var selectedMca by remember { mutableStateOf("") }
        var expandedType by remember { mutableStateOf(false) }
        var expandedMca by remember { mutableStateOf(false) }
        Dialog(onDismissRequest = onDismiss) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.4f)
            ) {
                Text(
                    text = "Searching for devices",
                    modifier = Modifier
                        .padding(16.dp),
                    textAlign = TextAlign.Center,
                )
                Divider()

                Column {
                    TextField(
                        modifier = Modifier.fillMaxWidth(1f),
                        value = selectedOption.i18Name,
                        onValueChange = {  },
                        label = { Text("设备类型") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text
                        ),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { expandedType = true }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                    DropdownMenu(
                        modifier = Modifier.fillMaxWidth(0.84f),
                        expanded = expandedType,
                        onDismissRequest = { expandedType = false }
                    ) {
                        Devices.values().forEach {
                            DropdownMenuItem(text = { Text(text = it.i18Name) }, onClick = {
                                selectedOption = it
                                expandedType = false
                            })
                        }
                    }
                }


                Column {
                    TextField(
                        modifier = Modifier.fillMaxWidth(1f),
                        value = selectedMca,
                        onValueChange = {  },
                        label = { Text("设备选择") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text
                        ),
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { expandedMca = true }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                    DropdownMenu(
                        modifier = Modifier.fillMaxWidth(0.84f),
                        expanded = expandedMca,
                        onDismissRequest = { expandedMca = false }
                    ) {
                        devices.filter { it.device.name == selectedOption.bluetoothName && it.device.address !in availabilityDevice.map { it.device.address } }.forEach {
                            DropdownMenuItem(text = { Text(text = it.device.address) }, onClick = {
                                selectedMca = it.device.address
                                expandedMca = false
                            })
                        }
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Divider(modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(top = 30.dp, bottom = 10.dp))

                    Button(
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                viewModel.bluetoothDevices.value[selectedMca]?.let {
                                    selectedOption.getDevice(context, viewModel, it.device)
                                }
                            }
                            show.value = false
                        }
                    ) {
                        Text(text = "添加设备")
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(navController: NavController) {
        TopAppBar(
            title = { Text(text = "Devices List") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            },
        )
    }
}