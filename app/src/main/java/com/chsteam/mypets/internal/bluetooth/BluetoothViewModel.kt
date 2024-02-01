package com.chsteam.mypets.internal.bluetooth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.chsteam.mypets.internal.compatibility.Device
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice


class BluetoothViewModel : ViewModel() {

    val bluetoothDevices = mutableStateOf<List<BleDevice>>(emptyList())

    val availabilityDevice = mutableStateOf<List<Device>>(emptyList())

    fun endBleScanner() {
        BleManager.getInstance().cancelScan()
    }

    fun initBleScanner() {
        BleManager.getInstance().scan(object : BleScanCallback() {
            override fun onScanStarted(success: Boolean) {

            }
            override fun onScanning(bleDevice: BleDevice) {
                bluetoothDevices.value = bluetoothDevices.value + bleDevice
            }
            override fun onScanFinished(scanResultList: List<BleDevice>) {
                bluetoothDevices.value = scanResultList
                BleManager.getInstance().cancelScan()
            }
        })
    }

    override fun onCleared() {
        this.availabilityDevice.value.forEach {
            BleManager.getInstance().disconnect(it.bleDevice)
            it.stopTicker()
        }
    }



}