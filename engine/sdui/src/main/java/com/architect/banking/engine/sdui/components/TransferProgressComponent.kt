package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class TransferProgressProps(
    val progress: Float = 0.5f,
    val activeColor: String = "TealAccent",
    val inactiveColor: String = "LightGray",
    val height: Int = 3,
)

@Composable
fun TransferProgressComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<TransferProgressProps>(props)
    }.getOrDefault(TransferProgressProps())

    val active = when (decoded.activeColor.lowercase()) {
        "tealaccent", "teal" -> ArchitectColors.TealAccent
        "navyprimary" -> ArchitectColors.NavyPrimary
        "goldaccent" -> ArchitectColors.GoldAccent
        else -> ArchitectColors.TealAccent
    }
    val inactive = when (decoded.inactiveColor.lowercase()) {
        "lightgray", "light_gray" -> ArchitectColors.LightGray
        "mediumgray" -> ArchitectColors.MediumGray
        else -> ArchitectColors.LightGray
    }
    val h = decoded.height.dp
    val progress = decoded.progress.coerceIn(0f, 1f)

    Row(modifier = Modifier.fillMaxWidth().height(h).clip(RoundedCornerShape(h / 2))) {
        Box(modifier = Modifier.weight(progress).height(h).background(active))
        if (progress < 1f) {
            Box(modifier = Modifier.weight(1f - progress).height(h).background(inactive))
        }
    }
}
