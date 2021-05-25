package me.rerere.polymartapp.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.google.accompanist.coil.rememberCoilPainter
import me.rerere.polymartapp.R

@Composable
fun Avatar(modifier: Modifier = Modifier, avatarUrl: String){
    val painter = rememberCoilPainter(request = avatarUrl, fadeIn = true, previewPlaceholder = R.drawable.logo)
    Box(modifier = Modifier.clip(CircleShape).background(Color.LightGray)){
        Image(modifier = modifier, painter = painter, contentDescription = "avatar")
    }
}