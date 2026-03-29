package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTypography
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.math.cos
import kotlin.math.sin

/** Props for the HEADER_BAR SDUI component. */
@Serializable
data class HeaderBarComponentProps(
    val title: String = "",
    val iconBackground: String = "NavyPrimary",
    val icon: String? = null,
    val applyStatusBarPadding: Boolean = false,
    val showSearch: Boolean = false,
    val showNotification: Boolean = false,
    val searchAction: String = "HEADER_SEARCH",
    val notificationAction: String = "HEADER_NOTIFICATION",
    val showBack: Boolean = false,
    val backAction: String = "NAVIGATE_BACK",
)

private fun String.isUrl() = startsWith("http://") || startsWith("https://")

/**
 * Draws the SynchBank brand icon — a gold hexagonal badge with a white bank
 * building and two sync arcs — at [size]. Used in HEADER_BAR when `icon = "bank"`.
 */
@Composable
private fun BankLogo(size: Dp) {
    Box(modifier = Modifier.size(size)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cx = this.size.width / 2
            val cy = this.size.height / 2
            val s = this.size.minDimension

            drawHexBadge(cx, cy, radius = s * 0.46f)
            drawSyncArcs(cx, cy, radius = s * 0.44f)
            drawBankBuilding(cx, cy, scale = s / 36f)
        }
    }
}

private fun DrawScope.drawHexBadge(cx: Float, cy: Float, radius: Float) {
    val path = Path()
    for (i in 0..5) {
        val angle = Math.toRadians(60.0 * i - 30.0)
        val x = cx + radius * cos(angle).toFloat()
        val y = cy + radius * sin(angle).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(
        path = path,
        brush = Brush.linearGradient(
            colors = listOf(Color(0xFFF0D06A), Color(0xFFC9A028), Color(0xFF8C6A10)),
            start = Offset(cx - radius, cy - radius),
            end = Offset(cx + radius, cy + radius),
        ),
    )
    drawPath(path = path, color = Color(0xFFF5E080), style = Stroke(width = 0.8.dp.toPx()))
}

private fun DrawScope.drawSyncArcs(cx: Float, cy: Float, radius: Float) {
    val stroke = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)
    val arcColor = Color.White.copy(alpha = 0.7f)
    val r2 = radius * 0.78f
    // Arc 1: top-right quarter
    drawArc(
        color = arcColor,
        startAngle = -90f,
        sweepAngle = 130f,
        useCenter = false,
        topLeft = Offset(cx - r2, cy - r2),
        size = Size(r2 * 2, r2 * 2),
        style = stroke,
    )
    // Arc 2: bottom-left quarter
    drawArc(
        color = arcColor,
        startAngle = 90f,
        sweepAngle = 130f,
        useCenter = false,
        topLeft = Offset(cx - r2, cy - r2),
        size = Size(r2 * 2, r2 * 2),
        style = stroke,
    )
}

private fun DrawScope.drawBankBuilding(cx: Float, cy: Float, scale: Float) {
    val w = Color.White
    // Roof triangle
    val roof = Path().apply {
        moveTo(cx - 7f * scale, cy)
        lineTo(cx, cy - 7f * scale)
        lineTo(cx + 7f * scale, cy)
        close()
    }
    drawPath(roof, w)
    // Beam
    drawRect(w, topLeft = Offset(cx - 8f * scale, cy), size = Size(16f * scale, 2.5f * scale))
    // Three columns
    val colW = 2.2f * scale
    val colH = 5.5f * scale
    val colTop = cy + 2.5f * scale
    for (i in -1..1) {
        drawRect(w, topLeft = Offset(cx + i * 4.5f * scale - colW / 2, colTop), size = Size(colW, colH))
    }
    // Base
    drawRect(w, topLeft = Offset(cx - 8f * scale, colTop + colH), size = Size(16f * scale, 2f * scale))
}

/**
 * Renders a HEADER_BAR SDUI component.
 *
 * Layout: [avatar] [title]  ···  [search icon] [notification icon]
 *
 * The left icon renders as a circular avatar — either loaded via Coil (URL)
 * or shown as the [Icons.Outlined.AccountCircle] placeholder.
 */
@Composable
fun HeaderBarComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<HeaderBarComponentProps>(props)
    }.getOrDefault(HeaderBarComponentProps())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (decoded.applyStatusBarPadding) Modifier.statusBarsPadding() else Modifier)
            .background(ArchitectColors.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // ── Left: back-or-avatar + title ─────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (decoded.showBack) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { onAction(decoded.backAction) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = "Back",
                        tint = ArchitectColors.NavyPrimary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            } else {
                val icon = decoded.icon
                when {
                    icon == "bank" -> BankLogo(size = 36.dp)
                    !icon.isNullOrBlank() && icon.isUrl() -> AsyncImage(
                        model = icon,
                        contentDescription = "Bank logo",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                    )
                    else -> Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "User avatar",
                        tint = ArchitectColors.NavyPrimary,
                        modifier = Modifier.size(36.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = decoded.title,
                style = ArchitectTypography.Heading3.copy(fontWeight = FontWeight.Bold),
                color = ArchitectColors.NavyPrimary,
            )
        }

        // ── Right: search + notification ──────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (decoded.showSearch) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { onAction(decoded.searchAction) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search",
                        tint = ArchitectColors.NavyPrimary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
            if (decoded.showNotification) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { onAction(decoded.notificationAction) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = ArchitectColors.NavyPrimary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        }
    }
}
