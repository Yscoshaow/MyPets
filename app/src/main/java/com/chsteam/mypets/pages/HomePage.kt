package com.chsteam.mypets.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.chsteam.mypets.api.config.quest.QuestPackage
import com.chsteam.mypets.internal.Instruction
import com.chsteam.mypets.internal.database.ChatViewModel
import com.chsteam.mypets.internal.database.Message
import com.chsteam.mypets.internal.database.Npc
import com.chsteam.mypets.internal.database.Post
import com.chsteam.mypets.internal.objectives.SelfObjective
import com.chsteam.mypets.internal.objectives.ShockObjective
import com.chsteam.mypets.internal.utils.convertImageToBase64
import java.util.Date

class HomePage : Page {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Main(navController: NavController) {

        val pagerState = rememberPagerState(
            initialPage = 0,
            initialPageOffsetFraction = 0f
        ) {
            3
        }

        val content: @Composable (PaddingValues) -> Unit = { padding ->
            Surface(modifier = Modifier.padding(padding)) {
                HorizontalPager(
                    state = pagerState,
                ) { page ->
                    when(page) {
                        0 -> ChattingList()
                        1 -> CurrentTask()
                        2 -> Share()
                    }
                }
            }
        }

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = { Sheet(navController) },

        ) {
            Scaffold(
                topBar = { TopBar() },
                bottomBar = { BottomBar(pagerState) },
                content = content
            )
        }
    }

    @Composable
    fun ChattingList() {
        val viewModel: ChatViewModel = viewModel()
        val latestMessages by viewModel.latestMessages.observeAsState(initial = emptyMap())
        LazyColumn {
            item {
                latestMessages.forEach {
                    MessageItem(it.key, it.value) {

                    }
                }
            }
        }
    }

    @Composable
    fun MessageItem(npc: Npc, message: Message, onClick: () -> Unit) {
        Row {
            Image(npc.imageBitmap.value, contentDescription = npc.name)
            Column {
                Text(text = npc.name)
                Text(text = message.message)
            }
            Divider()
        }
    }
    
    @Composable
    fun CurrentTask() {
        LazyColumn {
            item {
            }
        }
    }

    @Composable
    fun Share() {
        val context = LocalContext.current
        LazyColumn {
            item {
                Post(
                    0,
                    0,
                    "这是韶的新猫猫呢!",
                    listOf(convertImageToBase64(context, "quest/pictures/post1.png")),
                    Date()
                ).PostCard()
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar() {
        TopAppBar(title = { Text(text = "MyPets") })
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun BottomBar(pagerState: PagerState) {
        var selectedPage by remember { mutableStateOf(pagerState.currentPage) }

        NavigationBar {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Chat, contentDescription = "Chats") },
                label = { Text("Chats") },
                selected = pagerState.currentPage == 0,
                onClick = { selectedPage = 0 }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Task, contentDescription = "Tasks") },
                label = { Text("Tasks") },
                selected = pagerState.currentPage == 1,
                onClick = { selectedPage = 1 }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Share, contentDescription = "Share") },
                label = { Text("Share") },
                selected = pagerState.currentPage == 2,
                onClick = { selectedPage = 2 }
            )
        }
        LaunchedEffect(selectedPage) {
            pagerState.animateScrollToPage(selectedPage)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun Sheet(navController: NavController) {
        ModalDrawerSheet(
            modifier = Modifier.fillMaxWidth(0.85f),
            drawerShape = RectangleShape
        ) {
            NavigationDrawerItem(
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Settings ,"Settings", modifier = Modifier.padding(end = 24.dp, start = 12.dp))
                        Text(text = "设置", fontSize = TextUnit(23f, TextUnitType.Sp))
                    }
                },
                shape = RectangleShape,
                selected = false,
                onClick = {
                    navController.navigate(Pages.SETTINGS.name)
                }
            )
            Divider()
            NavigationDrawerItem(
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Lock ,"Device", modifier = Modifier.padding(end = 24.dp, start = 12.dp))
                        Text(text = "设备", fontSize = TextUnit(23f, TextUnitType.Sp))
                    }
                },
                shape = RectangleShape,
                selected = false,
                onClick = {
                    navController.navigate(Pages.DEVICES.name)
                }
            )
        }
    }
}