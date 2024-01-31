package com.chsteam.mypets.internal.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

object PermissionManager {

    val BLUETOOTH_PERMISSION = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN
    )

    val LOCATION_PERMISSION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val READ_EXTERNAL_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


    fun hasPermissions(permissions: Array<String>, context: Context): Boolean {
        return permissions.all {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            } else {
                PermissionChecker.checkSelfPermission(context, it) == PermissionChecker.PERMISSION_GRANTED
            }
        }
    }

    fun requestPermissions(permissions: Array<String>, context: Context, launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>) {
        val permissionsToRequest = permissions.filter {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
            } else {
                PermissionChecker.checkSelfPermission(context, it) != PermissionChecker.PERMISSION_GRANTED
            }
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            launcher.launch(permissionsToRequest)
        }
    }
}