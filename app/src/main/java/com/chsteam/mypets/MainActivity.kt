package com.chsteam.mypets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.chsteam.mypets.internal.bluetooth.PetsBluetooth
import com.chsteam.mypets.pages.PageManager
import com.chsteam.mypets.ui.theme.MyPetsTheme
import com.clj.fastble.BleManager

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var INSTANCE: MainActivity
    }

    val petsBluetooth by lazy {
        PetsBluetooth(this.applicationContext)
    }

    init {
        INSTANCE = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        petsBluetooth.enableBluetooth(this)
        BleManager.getInstance().init(application)
        setContent {
            MyPetsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PageManager.Main()
                }
            }
        }
    }
}