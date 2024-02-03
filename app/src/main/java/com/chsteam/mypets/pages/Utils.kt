package com.chsteam.mypets.pages

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

object Utils {

    @Composable
    fun loadAvatarFromAssets(assetPath: String, modifier: Modifier = Modifier, size: Int= 7) {

        val path = if(assetPath.startsWith("file:///storage")) {
            Uri.parse(assetPath)
        } else {
            "file:///android_asset/$assetPath"
        }

        BoxWithConstraints {
            val imageSize = with(LocalDensity.current) { maxWidth / size }
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(path)
                    .build(),
                contentDescription = null,
                modifier = modifier
                    .size(imageSize)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                loading = {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                },
                error = {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
            )
        }
    }
}