package com.chsteam.mypets.pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.chsteam.mypets.core.conversation.Conversation
import com.chsteam.mypets.core.database.ChatViewModel
import com.chsteam.mypets.core.database.Message
import com.chsteam.mypets.core.database.Npc
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Date

class HomePage : Page, KoinComponent {

    private val viewModel: ChatViewModel by inject()

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
                Divider()
                HorizontalPager(
                    state = pagerState,
                ) { page ->
                    when(page) {
                        0 -> ChattingList(navController)
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
    fun ChattingList(navController: NavController) {
        val latestMessages by viewModel.latestMessages.observeAsState(initial = emptyMap())
        LazyColumn {
            item {
                latestMessages.forEach {
                    MessageItem(it.key, it.value, navController)
                }
                MessageItem(npc = Npc(0, "example_package", "yscos_shaow", "韶", "quest/npc/avatar/koyori.png", listOf("正在调教小猫咪")), message = Message(0, 0, "你好呀我的小猫咪", Date()), navController)
                MessageItem(npc = Npc(0, "example_package", "yin", "盈", "quest/npc/avatar/saga.png", listOf("韶的小猫咪")), message = Message(0, 0, "你是？", Date()), navController)
            }
        }
    }

    @Composable
    fun MessageItem(npc: Npc, message: Message, navController: NavController) {
        val coroutineScope = rememberCoroutineScope()
        Box(modifier = Modifier.clickable {
            viewModel.chattingNpc.value = npc
            navController.navigate(Pages.CHAT.name)
            coroutineScope.launch {
                viewModel.startChat()
            }
        }) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight()) {
                npc.ShowAvatar(modifier = Modifier.padding(5.dp))
                Column(modifier = Modifier
                    .fillMaxHeight()
                    .padding(start = 10.dp)) {
                    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
                        Text(text = npc.name, color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = message.message)
                    }
                    Divider()
                }
            }
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
            Divider()
            NavigationDrawerItem(
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.DataObject ,"Experimental", modifier = Modifier.padding(end = 24.dp, start = 12.dp))
                        Text(text = "实验性功能", fontSize = TextUnit(23f, TextUnitType.Sp))
                    }
                },
                shape = RectangleShape,
                selected = false,
                onClick = {
                    navController.navigate(Pages.EXPERIMENTAL.name)
                }
            )
            Divider()
        }
    }
}