package com.chsteam.mypets.internal.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chsteam.mypets.MainActivity
import com.chsteam.mypets.internal.compatibility.Device
import com.chsteam.mypets.internal.permission.PermissionManager
import com.sscl.bluetoothlowenergylibrary.BleManager
import com.sscl.bluetoothlowenergylibrary.intefaces.OnBleScanListener

class BluetoothViewModel : ViewModel() {

    val bluetoothDevices = mutableStateOf<Map<String, ScanResult>>(emptyMap())

    val availabilityDevice = mutableStateOf<List<Device>>(emptyList())

    fun endBleScanner() {
        BleManager.releaseBleScannerInstance()
    }

    fun initBleScanner() {
        val bleScanner = BleManager.getBleScannerInstance()
        bleScanner.setOnBleScanStateChangedListener(onBleScanListener(this))
        bleScanner.startScan()
    }

    class onBleScanListener(private val bluetoothViewModel: BluetoothViewModel) : OnBleScanListener {
        override fun onBatchScanResults(results: List<ScanResult>) {

        }

        override fun onScanComplete() {
            BleManager.releaseBleScannerInstance()
        }

        override fun onScanFailed(errorCode: Int) {
            BleManager.releaseBleScannerInstance()
        }

        @SuppressLint("MissingPermission")
        override fun onScanFindOneNewDevice(scanResult: ScanResult) {
            bluetoothViewModel.bluetoothDevices.value = bluetoothViewModel.bluetoothDevices.value + Pair(scanResult.device.address, scanResult)
            Log.i("MyPets", "Found Device ${scanResult.device.name}")
        }
    }
}