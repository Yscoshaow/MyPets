package com.chsteam.mypets.pages

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
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
import com.chsteam.mypets.core.permission.PermissionManager
import com.chsteam.mypets.core.permission.PermissionManager.getUriFromSharedPreferences
import com.chsteam.mypets.core.permission.PermissionManager.hasPersistableUriPermission
import com.chsteam.mypets.core.permission.PermissionManager.saveUriToSharedPreferences

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
            }
        )
    }

    @Composable
    fun SettingsList(innerPadding: PaddingValues) {
        val context = LocalContext.current
        Column(modifier = Modifier.padding(innerPadding)) {
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(56.dp)) {
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

                Text(text = "蓝牙", fontSize = TextUnit(23f, TextUnitType.Sp), modifier = Modifier.padding(start = 36.dp).weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                Divider(modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
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
                Spacer(modifier = Modifier.padding(end = 36.dp))
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(56.dp)) {
                var checked by remember { mutableStateOf(PermissionManager.hasPermissions(PermissionManager.CAMERA_PERMISSION, context)) }

                val launcher =
                    rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                        if (permissions.all { it.value }) {
                            checked = true
                        } else {
                            checked = false
                            Toast.makeText(
                                context,
                                "We need Camera to take photo for post",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                Text(text = "相机", fontSize = TextUnit(23f, TextUnitType.Sp), modifier = Modifier.padding(start = 36.dp).weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                Divider(modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        PermissionManager.requestPermissions(PermissionManager.CAMERA_PERMISSION, context, launcher)
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
                Spacer(modifier = Modifier.padding(end = 36.dp))
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(56.dp)) {
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

                Text(text = "位置", fontSize = TextUnit(23f, TextUnitType.Sp), modifier = Modifier.padding(start = 36.dp).weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                Divider(modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
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
                Spacer(modifier = Modifier.padding(end = 36.dp))
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(56.dp)) {
                val uri = getUriFromSharedPreferences(context)

                var checked by remember { mutableStateOf(hasPersistableUriPermission(context, uri)) }
                var selectedDirectory by remember { mutableStateOf(uri) }

                val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree()) { uri ->
                    if (uri != null) {
                        context.contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                        selectedDirectory = uri
                        saveUriToSharedPreferences(context, uri)
                        checked = true
                    }
                }

                Text(text = "文件", fontSize = TextUnit(23f, TextUnitType.Sp), modifier = Modifier.padding(start = 36.dp).weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                Divider(modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        launcher.launch(null)
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
                Spacer(modifier = Modifier.padding(end = 36.dp))
            }
            Divider()
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.height(56.dp)) {
                var checked by remember { mutableStateOf(loadDefaultQuest(context)) }

                Text(text = "默认任务", fontSize = TextUnit(23f, TextUnitType.Sp), modifier = Modifier.padding(start = 36.dp).weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                Divider(modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
                    .padding(vertical = 8.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        saveDefaultQuest(context, it)
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
                Spacer(modifier = Modifier.padding(end = 36.dp))
            }
            Divider()
        }
    }

    private fun loadDefaultQuest(context: Context) : Boolean {
        val sharedPreferences = context.getSharedPreferences("mypets_settings", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("default_quest", false)
    }

    private fun saveDefaultQuest(context: Context, isLoadDefaultQuest: Boolean) {
        val sharedPreferences = context.getSharedPreferences("mypets_settings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean("default_quest", isLoadDefaultQuest)
            apply()
        }
    }
}