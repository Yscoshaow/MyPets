package com.chsteam.mypets.internal.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getSystemService

class PetsBluetooth(private val context: Context) {

    companion object {
        private const val REQUEST_ENABLE_BT = 1
    }

    lateinit var bluetoothAdapter: BluetoothAdapter

    private var enable = false
        private set

    fun enableBluetooth(activity: Activity) {
        val bluetoothManager: BluetoothManager? = getSystemService(context, BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter = bluetoothManager?.adapter ?: return

        this.bluetoothAdapter = bluetoothAdapter

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(activity, enableBtIntent, REQUEST_ENABLE_BT, null)
        }

        enable = bluetoothAdapter.isEnabled
    }
}