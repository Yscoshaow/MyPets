package com.chsteam.mypets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.room.Room
import com.chsteam.mypets.internal.bluetooth.PetsBluetooth
import com.chsteam.mypets.internal.database.MyPetsDatabase
import com.chsteam.mypets.pages.PageManager
import com.chsteam.mypets.ui.theme.MyPetsTheme
import com.sscl.bluetoothlowenergylibrary.BleManager
import java.io.BufferedReader

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
        BleManager.initialize(this)
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

    private fun loadJsonFromAssets() {
        val assetManager = assets
    }
}