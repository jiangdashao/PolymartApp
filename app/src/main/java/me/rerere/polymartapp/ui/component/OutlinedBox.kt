package me.rerere.polymartapp.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedBox(borderColor: Color, content: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)){
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .border(1.dp, borderColor, RoundedCornerShape(50))
                .padding(horizontal = 5.dp, vertical = 1.dp)
        ) {
            content()
        }
    }
}