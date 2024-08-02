package com.chsteam.mypets

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceScreen
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.glance.appwidget.GlanceAppWidget
import com.chsteam.mypets.core.bluetooth.PetsBluetooth
import com.chsteam.mypets.core.config.QuestManager
import com.chsteam.mypets.pages.PageManager
import com.chsteam.mypets.ui.theme.MyPetsTheme
import com.clj.fastble.BleManager
import me.zhanghai.compose.preference.PreferenceTheme
import me.zhanghai.compose.preference.ProvidePreferenceLocals

class MainActivity : ComponentActivity() {

    companion object {
        lateinit var INSTANCE: MainActivity
    }

    val petsBluetooth by lazy {
        PetsBluetooth(this.applicationContext)
    }

    lateinit var questManager: QuestManager

    init {
        INSTANCE = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        petsBluetooth.enableBluetooth(this)
        BleManager.getInstance().init(application)
        questManager = QuestManager(this.applicationContext)
        setContent {
            MyPetsTheme {
                ProvidePreferenceLocals {
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
}