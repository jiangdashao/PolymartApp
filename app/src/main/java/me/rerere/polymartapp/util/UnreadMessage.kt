package me.rerere.polymartapp.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

fun Modifier.unread(show: Boolean): Modifier{
    if(show) {
        return Modifier.drawWithContent {
            drawContent()
            drawCircle(
                color = Color.Red,
                radius = 5.dp.toPx(),
                center = Offset(size.width - 0.5.dp.toPx(), 0.5.dp.toPx()),
            )
        }
    }
    return this
}