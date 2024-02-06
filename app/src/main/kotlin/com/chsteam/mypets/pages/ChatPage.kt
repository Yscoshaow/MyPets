package com.chsteam.mypets.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.chsteam.mypets.core.conversation.Conversation
import com.chsteam.mypets.core.database.ChatViewModel
import com.chsteam.mypets.core.database.Message
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChatPage : Page, KoinComponent {

    val viewModel: ChatViewModel by inject()

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
            item {
                if(viewModel.chattingNpc.value != null) {
                    viewModel.allMessage.value?.get(viewModel.chattingNpc.value)?.forEach { message: Message ->
                        Spacer(modifier = Modifier.padding(5.dp))
                        MessageBubbleNpc(message = message.message)
                    }
                }
            }
        }
    }

    @Composable
    fun MessageBubbleNpc(message: String) {
        Row(Modifier.padding(end = 70.dp, start = 5.dp)) {
            val color = MaterialTheme.colorScheme.secondaryContainer
            Text(text = message, modifier = Modifier
                .drawBehind {
                    val bubble = Path().apply {
                        val rect = RoundRect(
                            10.dp.toPx(),
                            0f,
                            size.width - 10.dp.toPx(),
                            size.height,
                            15.dp.toPx(),
                            15.dp.toPx()
                        )
                        addRoundRect(rect)
                        moveTo(10.dp.toPx(), 15.dp.toPx())
                        lineTo(5.dp.toPx(), 20.dp.toPx())
                        lineTo(10.dp.toPx(), 25.dp.toPx())
                        close()
                    }
                    drawPath(bubble, color)
                }
                .padding(20.dp, 10.dp))
        }
    }

    @Composable
    fun MessageBubbleYour(message: String) {
        Row(Modifier.padding(start = 70.dp, end = 5.dp)) {
            val color = MaterialTheme.colorScheme.secondaryContainer
            Spacer(modifier = Modifier.weight(1f))
            Text(text = message, modifier = Modifier
                .drawBehind {
                    val bubble = Path().apply {
                        val rect = RoundRect(
                            10.dp.toPx(),
                            0f,
                            size.width - 10.dp.toPx(),
                            size.height,
                            15.dp.toPx(),
                            15.dp.toPx()
                        )
                        addRoundRect(rect)
                        moveTo(size.width - 10.dp.toPx(), 15.dp.toPx())
                        lineTo(size.width - 5.dp.toPx(), 20.dp.toPx())
                        lineTo(size.width - 10.dp.toPx(), 25.dp.toPx())
                        close()
                    }
                    drawPath(bubble, color)
                }
                .padding(20.dp, 10.dp))
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(navController: NavController, snackbarHostState: SnackbarHostState) {
        val scope = rememberCoroutineScope()
        TopAppBar(
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    viewModel.chattingNpc.value?.ShowAvatar(Modifier.padding(5.dp))
                    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(start = 10.dp)) {
                        Text(text = viewModel.chattingNpc.value?.name ?: "", color = MaterialTheme.colorScheme.primary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = viewModel.chattingNpc.value?.description?.get(0) ?: "", fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("${viewModel.chattingNpc.value?.name}没有给予你她的电话权限", actionLabel = "Error")
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
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)) {
            var showList by remember { mutableStateOf(false) }

            if(!showList) {
                Row(modifier = Modifier
                    .clickable { showList = !showList }
                    .fillMaxWidth()
                    .fillMaxHeight(0.06f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "输入消息", color = MaterialTheme.colorScheme.secondary, modifier = Modifier.padding(start = 35.dp), fontSize = 20.sp)
                }

                Divider()
            }

            if(showList) {
                Spacer(modifier = Modifier.padding(vertical = 5.dp))
                LazyColumn {
                    item {
                        for(i in 0 until  viewModel.responseMessage.value.size) {
                            Options(text = viewModel.responseMessage.value[i], number = i)
                        }
                    }
                }
            }

        }
    }


    @Composable
    fun Options(text: String, number: Int) {
        Row(
            Modifier
                .fillMaxWidth(1f)
                .clickable {
                    viewModel.chattingNpc.value?.let { Conversation.getOrCreateConversation(it).passPlayerAnswer(number = number) }
                }
        ) {
            val color = MaterialTheme.colorScheme.primary
            Text(text = text, modifier = Modifier
                .drawBehind {
                    val bubble = Path().apply {
                        val rect = RoundRect(
                            10.dp.toPx(),
                            0f,
                            size.width - 10.dp.toPx(),
                            size.height,
                            15.dp.toPx(),
                            15.dp.toPx()
                        )
                        addRoundRect(rect)
                        close()
                    }
                    drawPath(bubble, color)
                }
                .fillMaxWidth()
                .padding(20.dp, 20.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 3.dp))
    }
}