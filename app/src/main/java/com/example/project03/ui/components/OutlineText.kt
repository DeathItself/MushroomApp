package com.example.project03.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp


@Composable
fun OutLineText(text: String) {
    val size = 65f
    val textPaintStroke = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        style = android.graphics.Paint.Style.STROKE
        textSize = size
        color = android.graphics.Color.BLACK
        strokeWidth = 10f
    }

    val textPaintFill = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        style = android.graphics.Paint.Style.FILL
        textSize = size
        color = android.graphics.Color.WHITE
    }

    val textWidth = textPaintStroke.measureText(text)

    Layout(
        content = {
            Canvas(
                modifier = Modifier.size(width = textWidth.dp, height = size.dp)
            ) {
                drawIntoCanvas { canvas ->
                    val x = 0f
                    val y = textPaintFill.fontMetrics.run { -ascent }
                    canvas.nativeCanvas.drawText(text, x, y, textPaintStroke)
                    canvas.nativeCanvas.drawText(text, x, y, textPaintFill)
                }
            }
        }
    ) { measurables, constraints ->
        val placeable = measurables.first().measure(constraints.copy(maxWidth = textWidth.toInt(), maxHeight = size.toInt()))

        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}