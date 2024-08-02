package com.chsteam.mypets.core.permission

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker

object PermissionManager {

    val RECORD_AUDIO_PERMISSION = arrayOf(
        Manifest.permission.RECORD_AUDIO
    )

    val LOCATION_PERMISSION = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_BACKGROUND_LOCATION
    )

    val CAMERA_PERMISSION = arrayOf(
        Manifest.permission.CAMERA
    )

    fun getBluetoothPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_SCAN
            )
        } else {
            arrayOf(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN
            )
        }
    }

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

    fun saveUriToSharedPreferences(context: Context, uri: Uri) {
        val sharedPreferences = context.getSharedPreferences("mypets_settings", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("selected_directory_uri", uri.toString())
            apply()
        }
    }

    fun getUriFromSharedPreferences(context: Context): Uri? {
        val sharedPreferences = context.getSharedPreferences("mypets_settings", Context.MODE_PRIVATE)
        val uriString = sharedPreferences.getString("selected_directory_uri", null)
        return uriString?.let { Uri.parse(it) }
    }

    fun hasPersistableUriPermission(context: Context, uriToCheck: Uri?): Boolean {
        if(uriToCheck == null) return false
        val persistedUriPermissions = context.contentResolver.persistedUriPermissions
        return persistedUriPermissions.any {
            it.uri == uriToCheck && it.isReadPermission && it.isWritePermission
        }
    }
}