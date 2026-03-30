package com.architect.banking

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

class SplashActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SplashScreen {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }
}

@Composable
private fun SplashScreen(onSplashComplete: () -> Unit) {
    val background = Brush.verticalGradient(
        colors = listOf(Color(0xFF0A1F5C), Color(0xFF051030), Color(0xFF020818)),
    )

    var started by remember { mutableStateOf(false) }
    var taglineVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        started = true
        delay(600)
        taglineVisible = true
        delay(2000)
        onSplashComplete()
    }

    // Outer rings rotation
    val infiniteTransition = rememberInfiniteTransition(label = "sync_rings")
    val outerRingRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "outer_ring",
    )
    val innerRingRotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "inner_ring",
    )

    // Pulse glow
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 0.40f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pulse",
    )

    // Logo entrance
    val logoScale by animateFloatAsState(
        targetValue = if (started) 1f else 0.3f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "logo_scale",
    )
    val logoAlpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        label = "logo_alpha",
    )
    val taglineAlpha by animateFloatAsState(
        targetValue = if (taglineVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "tagline_alpha",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background),
        contentAlignment = Alignment.Center,
    ) {
        // Ambient pulse glow behind logo
        Canvas(modifier = Modifier.size(220.dp)) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF1A4BCC).copy(alpha = pulseAlpha),
                        Color.Transparent,
                    ),
                    center = center,
                    radius = size.minDimension / 2,
                ),
                radius = size.minDimension / 2,
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Logo canvas — sync rings + bank icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(160.dp)
                    .scale(logoScale)
                    .alpha(logoAlpha),
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2
                    val cy = size.height / 2

                    // Outer sync ring (rotating arcs with arrowheads)
                    rotate(degrees = outerRingRotation, pivot = Offset(cx, cy)) {
                        drawSyncRing(
                            cx = cx,
                            cy = cy,
                            radius = size.minDimension / 2 - 8.dp.toPx(),
                            color = Color(0xFFE8B84B),
                            strokeWidth = 5.dp.toPx(),
                            arcSweep = 155f,
                        )
                    }

                    // Inner sync ring (counter-rotating)
                    rotate(degrees = innerRingRotation, pivot = Offset(cx, cy)) {
                        drawSyncRing(
                            cx = cx,
                            cy = cy,
                            radius = size.minDimension / 2 - 22.dp.toPx(),
                            color = Color(0xFF4A90E2).copy(alpha = 0.85f),
                            strokeWidth = 3.dp.toPx(),
                            arcSweep = 130f,
                        )
                    }

                    // Hexagonal badge in center
                    drawHexBadge(cx = cx, cy = cy, radius = 38.dp.toPx())

                    // Bank building
                    drawBankBuilding(cx = cx, cy = cy, scale = size.minDimension / 160f)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App name
            Text(
                text = "SynchBank",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.alpha(logoAlpha),
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Your money, in sync.",
                color = Color(0xFFE8B84B),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(taglineAlpha),
            )
        }
    }
}

/** Draws two 155° arcs with arrowheads at their tips, forming a circular sync symbol. */
private fun DrawScope.drawSyncRing(
    cx: Float,
    cy: Float,
    radius: Float,
    color: Color,
    strokeWidth: Float,
    arcSweep: Float,
) {
    val gap = 360f - arcSweep * 2
    val halfGap = gap / 2f

    // Arc 1
    val arc1Start = -90f + halfGap / 2f
    drawArc(
        color = color,
        startAngle = arc1Start,
        sweepAngle = arcSweep,
        useCenter = false,
        topLeft = Offset(cx - radius, cy - radius),
        size = Size(radius * 2, radius * 2),
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
    )
    // Arc 1 arrowhead
    val arc1End = Math.toRadians((arc1Start + arcSweep).toDouble())
    drawArrowhead(cx, cy, radius, arc1End.toFloat(), color, strokeWidth * 0.9f, clockwise = true)

    // Arc 2 (offset by 180°)
    val arc2Start = arc1Start + 180f
    drawArc(
        color = color,
        startAngle = arc2Start,
        sweepAngle = arcSweep,
        useCenter = false,
        topLeft = Offset(cx - radius, cy - radius),
        size = Size(radius * 2, radius * 2),
        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
    )
    val arc2End = Math.toRadians((arc2Start + arcSweep).toDouble())
    drawArrowhead(cx, cy, radius, arc2End.toFloat(), color, strokeWidth * 0.9f, clockwise = true)
}

/** Draws a small filled arrowhead pointing in the clockwise tangent direction at [angleRad]. */
private fun DrawScope.drawArrowhead(
    cx: Float,
    cy: Float,
    radius: Float,
    angleRad: Float,
    color: Color,
    size: Float,
    clockwise: Boolean,
) {
    val tipX = cx + radius * cos(angleRad)
    val tipY = cy + radius * sin(angleRad)

    // Tangent direction (clockwise)
    val sign = if (clockwise) 1f else -1f
    val tx = sign * (-sin(angleRad))
    val ty = sign * cos(angleRad)

    // Perpendicular (normal, outward)
    val nx = cos(angleRad)
    val ny = sin(angleRad)

    val len = size * 1.8f
    val half = size * 0.7f

    val path = Path().apply {
        moveTo(tipX + tx * len / 2, tipY + ty * len / 2)
        lineTo(tipX - tx * len / 2 + nx * half, tipY - ty * len / 2 + ny * half)
        lineTo(tipX - tx * len / 2 - nx * half, tipY - ty * len / 2 - ny * half)
        close()
    }
    drawPath(path = path, color = color)
}

/** Draws a filled hexagonal badge with a gold gradient. */
private fun DrawScope.drawHexBadge(cx: Float, cy: Float, radius: Float) {
    val path = Path()
    for (i in 0..5) {
        val angle = Math.toRadians((60.0 * i - 30.0))
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
    // Inner edge highlight
    drawPath(
        path = path,
        color = Color(0xFFF5E080),
        style = Stroke(width = 1.5.dp.toPx()),
    )
}

/** Draws a simplified bank building in white, scaled by [scale]. */
private fun DrawScope.drawBankBuilding(cx: Float, cy: Float, scale: Float) {
    val w = Color.White

    // Roof triangle
    val roofPath = Path().apply {
        moveTo(cx - 15f * scale, cy - 1f * scale)
        lineTo(cx, cy - 14f * scale)
        lineTo(cx + 15f * scale, cy - 1f * scale)
        close()
    }
    drawPath(roofPath, w)

    // Entablature beam
    drawRect(w, topLeft = Offset(cx - 17f * scale, cy - 1f * scale), size = Size(34f * scale, 5f * scale))

    // Three columns
    val colW = 5f * scale
    val colH = 12f * scale
    val colY = cy + 4f * scale
    for (i in -1..1) {
        drawRect(w, topLeft = Offset(cx + i * 10f * scale - colW / 2, colY), size = Size(colW, colH))
    }

    // Base
    drawRect(w, topLeft = Offset(cx - 17f * scale, colY + colH), size = Size(34f * scale, 4f * scale))
    drawRect(w, topLeft = Offset(cx - 14f * scale, colY + colH + 4f * scale), size = Size(28f * scale, 3.5f * scale))

    // Apex gold dot
    drawCircle(Color(0xFFC9A028), radius = 2.5f * scale, center = Offset(cx, cy - 12f * scale))
}
