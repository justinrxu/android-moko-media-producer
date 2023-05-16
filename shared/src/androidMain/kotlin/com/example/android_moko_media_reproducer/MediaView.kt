package com.example.android_moko_media_reproducer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import dev.icerock.moko.media.Bitmap
import dev.icerock.moko.media.compose.BindMediaPickerEffect
import dev.icerock.moko.media.compose.rememberMediaPickerControllerFactory
import dev.icerock.moko.media.compose.toImageBitmap
import dev.icerock.moko.media.picker.CanceledException
import dev.icerock.moko.media.picker.MediaSource
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import kotlinx.coroutines.launch

@Composable
fun MediaApp() {
    Navigator(MediaView())
}

internal class MediaView : Screen {
    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val mediaFactory = rememberMediaPickerControllerFactory()
        val picker = remember(mediaFactory) { mediaFactory.createMediaPickerController() }
        BindMediaPickerEffect(picker)

        var pfpBitmap: Bitmap? by remember { mutableStateOf(null) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Gray)
                .padding(vertical = 100.dp)
        ) {
            Box(
                modifier = Modifier
                    .border(width = 5.dp, color = Color.DarkGray)
            ) {
                pfpBitmap?.let { bitmap ->
                    Image(
                        bitmap = bitmap.toImageBitmap(),
                        contentDescription = "Selected picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxHeight(0.3f)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                    )
                } ?: Image(
                    painter = rememberVectorPainter(image = Icons.Default.Face),
                    contentDescription = "Selected picture",
                    modifier = Modifier
                        .fillMaxHeight(0.3f)
                        .aspectRatio(1f)
                        .clip(CircleShape)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            TextButton(
                modifier = Modifier
                    .width(150.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Green),
                shape = CircleShape,
                onClick = {
                    scope.launch {
                        try {
                            pfpBitmap = picker.pickImage(MediaSource.GALLERY)
                        } catch (exc: DeniedException) {
                            println("denied - $exc")
                        } catch (exc: CanceledException) {
                            println("cancelled - $exc")
                        }
                    }
                }
            ) {
                Text(
                    text = "Choose photo",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}