package com.chsteam.mypets.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

class ChatPage : Page {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Main(navController: NavController) {

        val snackbarHostState = remember { SnackbarHostState() }

        val content: @Composable (PaddingValues) -> Unit = { padding ->
            Surface(modifier = Modifier.padding(padding)) {
                Divider()
                MessageList()
            }
        }

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = { TopBar(navController, snackbarHostState) },
            bottomBar = { BottomBar() },
            content = content
        )
    }

    @Composable
    fun MessageList() {
        LazyColumn {

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(navController: NavController, snackbarHostState: SnackbarHostState) {
        val scope = rememberCoroutineScope()
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Utils.loadAvatarFromAssets(
                        assetPath = "quest/npc/avatar/koyori.png",
                        modifier = Modifier.padding(5.dp)
                    )
                    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(start = 10.dp)) {
                        Text(text = "韶", color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = "正在调教小猫", fontSize = 16.sp,)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("韶没有给予你她的电话权限", actionLabel = "Error")
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Phone, contentDescription = "")
                    }
                }
            },
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
    fun BottomBar() {

    }
}