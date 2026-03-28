package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the LINE_CHART SDUI component. */
@Serializable
data class LineChartComponentProps(
    val dataPoints: List<Double> = emptyList(),
    val lineColor: String = "Success",
    val height: Int = 120,
)

/**
 * Renders a LINE_CHART SDUI component as a smooth sparkline using Compose Canvas.
 */
@Composable
fun LineChartComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }
            .decodeFromJsonElement<LineChartComponentProps>(props)
    }.getOrDefault(LineChartComponentProps())

    val points = decoded.dataPoints
    if (points.size < 2) return

    val lineColor = decoded.lineColor.toChartColor()
    val fillColor = lineColor.copy(alpha = 0.12f)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(decoded.height.dp)
    ) {
        val minVal = points.min()
        val maxVal = points.max()
        val range = (maxVal - minVal).coerceAtLeast(1.0)

        val stepX = size.width / (points.size - 1).toFloat()
        val paddingTop = size.height * 0.1f
        val paddingBottom = size.height * 0.1f
        val chartHeight = size.height - paddingTop - paddingBottom

        fun xOf(i: Int) = i * stepX
        fun yOf(v: Double) = (paddingTop + chartHeight * (1.0 - (v - minVal) / range)).toFloat()

        // Build fill path
        val fillPath = Path().apply {
            moveTo(xOf(0), size.height)
            lineTo(xOf(0), yOf(points[0]))
            for (i in 1 until points.size) {
                val prevX = xOf(i - 1)
                val currX = xOf(i)
                val prevY = yOf(points[i - 1])
                val currY = yOf(points[i])
                val controlX = (prevX + currX) / 2f
                cubicTo(controlX, prevY, controlX, currY, currX, currY)
            }
            lineTo(xOf(points.size - 1), size.height)
            close()
        }
        drawPath(fillPath, color = fillColor)

        // Build line path
        val linePath = Path().apply {
            moveTo(xOf(0), yOf(points[0]))
            for (i in 1 until points.size) {
                val prevX = xOf(i - 1)
                val currX = xOf(i)
                val prevY = yOf(points[i - 1])
                val currY = yOf(points[i])
                val controlX = (prevX + currX) / 2f
                cubicTo(controlX, prevY, controlX, currY, currX, currY)
            }
        }
        drawPath(
            linePath,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
        )

        // Last point dot
        drawCircle(
            color = lineColor,
            radius = 5.dp.toPx(),
            center = Offset(xOf(points.size - 1), yOf(points.last())),
        )
    }
}

private fun String.toChartColor(): Color = when (this.uppercase()) {
    "TEALACCENT" -> ArchitectColors.TealAccent
    "TEALLIGHT" -> ArchitectColors.TealLight
    "SUCCESS" -> ArchitectColors.Success
    "NAVYPRIMARY" -> ArchitectColors.NavyPrimary
    "GOLDACCENT" -> ArchitectColors.GoldAccent
    "ERROR" -> ArchitectColors.Error
    "INFO" -> ArchitectColors.Info
    else -> ArchitectColors.TealAccent
}
