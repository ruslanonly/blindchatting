package com.example.blindchatting.shared.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Avatar(
    size: Dp = 48.dp,
    text: String
) {
    val initials = text.split(" ").joinToString("") { it.take(1) }.uppercase()

    val color = Color((text.hashCode() and 0x00FFFFFF) + 0xFF000000.toInt())

    Canvas(modifier = Modifier.size(size)) {
        drawCircle(color = color)
        drawInitials(size, initials)
    }
}

private fun DrawScope.drawInitials(
    textSize: Dp,
    initials: String
) {
    val textSizePx = textSize.toPx() / 3

    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.WHITE
        this.textSize = textSizePx
        textAlign = android.graphics.Paint.Align.CENTER
        typeface = android.graphics.Typeface.create("", android.graphics.Typeface.NORMAL)
    }

    val baseline = (size.height / 2) - (paint.descent() + paint.ascent()) / 2

    drawContext.canvas.nativeCanvas.apply {
        drawText(
            initials,
            size.width / 2,
            baseline,
            paint
        )
    }
}