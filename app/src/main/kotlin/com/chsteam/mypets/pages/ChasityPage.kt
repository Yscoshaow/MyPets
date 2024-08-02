package com.chsteam.mypets.pages

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.HideSource
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.chsteam.mypets.core.chasity.Chasity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.zhanghai.compose.preference.SwitchPreference
import org.koin.core.component.KoinComponent
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ChasityPage: Page, KoinComponent {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Main(navController: NavController) {

        val content: @Composable (PaddingValues) -> Unit = { innerPadding ->
            Chasity(innerPadding)
        }

        Scaffold(
            topBar = { TopBar(navController = navController) },
            content = content
        )
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun TopBar(navController: NavController) {
        TopAppBar(
            title = { Text(text = "Chasity") },
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
    fun Chasity(innerPadding: PaddingValues) {
        Column(modifier = Modifier.padding(innerPadding)) {
            SetChasity()
        }
    }

    @Composable
    fun SetChasity() {
        Column {
            TimePickerScreen()
            ChasityOption()
            ChasityModule()
        }
    }

    @Composable
    fun ChasityOption() {
        val isHideTime = remember { mutableStateOf(false) }
        Column {
            Text(
                text = "基本选项",
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
            ElevatedCard(modifier = Modifier.padding(5.dp)) {
                SwitchPreference(
                    state = remember { mutableStateOf(false) },
                    title = { Text(text = "隐藏时间") },
                    modifier = Modifier.fillMaxWidth(),
                    icon = { Icon(imageVector = Icons.Outlined.HideSource, contentDescription = null) },
                    summary = { Text(text = "这将隐藏此锁的剩余时间") }
                )
                HorizontalDivider()
                SwitchPreference(
                    state = remember { mutableStateOf(false) },
                    title = { Text(text = "清洁开锁") },
                    modifier = Modifier.fillMaxWidth(),
                    icon = { Icon(imageVector = Icons.Outlined.LockOpen, contentDescription = null) },
                    summary = { Text(text = "这将给予每天5分钟开锁时间") }
                )
            }
        }

    }

    @Composable
    fun ChasityModule() {
        Column {
            Text(
                text = "小组件",
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )

            ElevatedCard(modifier = Modifier.padding(5.dp)) {
                SwitchPreference(
                    state = remember { mutableStateOf(false) },
                    title = { Text(text = "图片验证模块") },
                    modifier = Modifier.fillMaxWidth(),
                    icon = { Icon(imageVector = Icons.Outlined.HideSource, contentDescription = null) },
                    summary = { Text(text = "这将要求你每天都需要拍摄锁的图片") }
                )
                HorizontalDivider()
                SwitchPreference(
                    state = remember { mutableStateOf(false) },
                    title = { Text(text = "随机时间模块") },
                    modifier = Modifier.fillMaxWidth(),
                    icon = { Icon(imageVector = Icons.Outlined.HideSource, contentDescription = null) },
                    summary = { Text(text = "时间每次更新时,会乘以你设定的区间中的一个值") }
                )
            }
        }
    }

    @Composable
    fun TimePickerScreen() {
        Column {
            Text(
                text = "初始持续时间",
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
            ElevatedCard(modifier = Modifier.padding(5.dp)) {
                var minDays by remember { mutableStateOf(4) }
                var minHours by remember { mutableStateOf(4) }
                var minMinutes by remember { mutableStateOf(3) }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TimeSelector(
                            title = "最短持续时间",
                            days = minDays,
                            hours = minHours,
                            minutes = minMinutes,
                            onDaysChange = { minDays = it },
                            onHoursChange = { minHours = it },
                            onMinutesChange = { minMinutes = it }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun TimeSelector(
        title: String,
        days: Int,
        hours: Int,
        minutes: Int,
        onDaysChange: (Int) -> Unit,
        onHoursChange: (Int) -> Unit,
        onMinutesChange: (Int) -> Unit
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = title, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                NumberPicker(value = days, onValueChange = onDaysChange)
                Text(text = "日", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 4.dp))
                NumberPicker(value = hours, onValueChange = onHoursChange)
                Text(text = "小时", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 4.dp))
                NumberPicker(value = minutes, onValueChange = onMinutesChange)
                Text(text = "分钟", fontSize = 16.sp, modifier = Modifier.padding(horizontal = 4.dp))
            }
        }
    }

    @Composable
    fun NumberPicker(value: Int, onValueChange: (Int) -> Unit) {
        var isIncreasing by remember { mutableStateOf(false) }
        var isDecreasing by remember { mutableStateOf(false) }

        val scope = rememberCoroutineScope()

        Column {
            IconButton(
                onClick = { if (value > 0) onValueChange(value - 1) },
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            scope.launch {
                                isDecreasing = true
                                delay(500L)  // 延迟以区分点击和长按
                                while (isDecreasing) {
                                    if (value > 0) onValueChange(value - 1)
                                    delay(100L)
                                }
                            }
                            tryAwaitRelease()
                            isDecreasing = false
                        }
                    )
                }
            ) {
                Icon(Icons.Default.Remove, contentDescription = null)
            }

            BasicText(
                text = value.toString().padStart(2, '0'),
                modifier = Modifier.width(24.dp)
            )

            IconButton(
                onClick = { onValueChange(value + 1) },
                modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            scope.launch {
                                isIncreasing = true
                                delay(500L)  // 延迟以区分点击和长按
                                while (isIncreasing) {
                                    onValueChange(value + 1)
                                    delay(100L)
                                }
                            }
                            tryAwaitRelease()
                            isIncreasing = false
                        }
                    )
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    }


    @Composable
    fun DisplayChasity() {
        val startDate = LocalDate.of(2023, 1, 1)
        val endDate = LocalDate.of(2024, 12, 31)
        ProgressRing(startDate, endDate)
    }

    @Composable
    fun ProgressRing(startDate: LocalDate, endDate: LocalDate) {

        val containerColor = MaterialTheme.colorScheme.primaryContainer
        val primaryColor = MaterialTheme.colorScheme.primary

        val currentDate = LocalDateTime.now()
        val startDateTime = startDate.atStartOfDay()
        val endDateTime = endDate.atStartOfDay()

        val totalDays = ChronoUnit.DAYS.between(startDateTime, endDateTime).toFloat()
        val passedDays = ChronoUnit.DAYS.between(startDateTime, currentDate).toFloat()
        val progress = (passedDays / totalDays).coerceIn(0f, 1f)

        val animatedProgress = animateFloatAsState(targetValue = progress)

        var remainingTime by remember { mutableStateOf(Duration.between(currentDate, endDateTime)) }

        LaunchedEffect(Unit) {
            while (true) {
                remainingTime = Duration.between(currentDate, endDateTime)
                delay(1000L)
            }
        }

        val days = remainingTime.toDays()
        val hours = remainingTime.toHours() % 24
        val minutes = remainingTime.toMinutes() % 60
        val seconds = remainingTime.seconds % 60

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(200.dp)) {
                drawArc(
                    color = containerColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 40f, cap = StrokeCap.Round)
                )
                drawArc(
                    color = primaryColor,
                    startAngle = -90f,
                    sweepAngle = 360 * animatedProgress.value,
                    useCenter = false,
                    style = Stroke(width = 40f, cap = StrokeCap.Round)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentDate.isAfter(endDateTime)) {
                    Text(
                        text = "unLock",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                } else {
                    Spacer(modifier = Modifier.width(36.dp))
                    Text(
                        text = "Lock",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(36.dp))
                    Text(
                        text = "剩余时间",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "${days}天 ${hours}小时",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "${minutes}分 ${seconds}秒",
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}