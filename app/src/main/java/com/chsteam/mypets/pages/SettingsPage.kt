package com.chsteam.mypets.pages

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.chsteam.mypets.internal.permission.PermissionManager

class SettingsPage : Page {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Main(navController: NavController) {

        val content: @Composable (PaddingValues) -> Unit = { innerPadding ->
            SettingsList(innerPadding)
        }

        Scaffold(
            topBar = { TopBar(navController = navController) },
            content = content
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(navController: NavController) {
        TopAppBar(
            title = { Text(text = "Settings") },
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

    @Composable
    fun SettingsList(innerPadding: PaddingValues) {
        val context = LocalContext.current
        Column(modifier = Modifier.padding(innerPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight(0.09f)) {
                var checked by remember { mutableStateOf(PermissionManager.hasPermissions(PermissionManager.BLUETOOTH_PERMISSION, context)) }

                val launcher =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                        if (permissions.all { it.value }) {
                            checked = true
                        } else {
                            checked = false
                            Toast.makeText(
                                context,
                                "Bluetooth permissions are required to enable Bluetooth",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                Text(text = "蓝牙", fontSize = TextUnit(23f, TextUnitType.Sp), modifier = Modifier.padding(start = 36.dp))
                Spacer(modifier = Modifier.fillMaxWidth(0.7f))
                Divider(modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        PermissionManager.requestPermissions(PermissionManager.BLUETOOTH_PERMISSION, context, launcher)
                        checked = it
                    },
                    thumbContent = if (checked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    }
                )
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight(0.09f)) {
                var checked by remember { mutableStateOf(false) }

                Text(text = "相机", fontSize = TextUnit(23f, TextUnitType.Sp), modifier = Modifier.padding(start = 36.dp))
                Spacer(modifier = Modifier.fillMaxWidth(0.7f))
                Divider(modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                    },
                    thumbContent = if (checked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    }
                )
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight(0.09f)) {
                var checked by remember { mutableStateOf(PermissionManager.hasPermissions(PermissionManager.LOCATION_PERMISSION, context)) }

                val launcher =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                        if (permissions.all { it.value }) {
                            checked = true
                        } else {
                            checked = false
                            Toast.makeText(
                                context,
                                "Bluetooth permissions are required to enable Bluetooth",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                Text(text = "位置", fontSize = TextUnit(23f, TextUnitType.Sp), modifier = Modifier.padding(start = 36.dp))
                Spacer(modifier = Modifier.fillMaxWidth(0.7f))
                Divider(modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.fillMaxWidth(0.1f))
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        PermissionManager.requestPermissions(PermissionManager.LOCATION_PERMISSION, context, launcher)
                        checked = it
                    },
                    thumbContent = if (checked) {
                        {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize),
                            )
                        }
                    } else {
                        null
                    }
                )
            }
            Divider()
        }
    }

}