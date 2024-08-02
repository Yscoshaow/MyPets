package com.chsteam.mypets.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.glance.text.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import kotlinx.coroutines.delay
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


class LockWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            // create your AppWidget here
            GlanceTheme {
                LockWidgetContent()
            }
        }
    }


    @Composable
    fun LockWidgetContent() {
        Row(
            modifier = GlanceModifier.fillMaxSize().
            background(GlanceTheme.colors.background),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val startDate = LocalDate.of(2023, 1, 1)
            val endDate = LocalDate.of(2024, 12, 31)
            ProgressRing(startDate = startDate, endDate = endDate)
        }
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
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (currentDate.isAfter(endDateTime)) {
                    Text(
                        text = "unLock",
                    )
                } else {
                    Text(
                        text = "Lock",
                    )
                }
            }
        }
    }
}

class LockWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = LockWidget()
}