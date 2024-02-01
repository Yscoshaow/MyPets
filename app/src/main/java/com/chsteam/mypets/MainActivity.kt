package com.chsteam.mypets

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.chsteam.mypets.internal.bluetooth.PetsBluetooth
import com.chsteam.mypets.internal.loader.QuestLoader
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
        QuestLoader.traverseAssets(this.applicationContext)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val directoryUri = data?.data
            // 使用该URI遍历文件夹
        }
    }
}