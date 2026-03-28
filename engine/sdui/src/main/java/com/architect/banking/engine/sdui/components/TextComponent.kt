package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTypography
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class TextComponentProps(
    val text: String = "",
    val style: String = "BODY",
    val color: String? = null,
    val align: String = "START",
)

@Composable
fun TextComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<TextComponentProps>(props)
    }.getOrDefault(TextComponentProps())

    val needsFullWidth = decoded.align.uppercase() != "START"
    Text(
        text = decoded.text,
        style = decoded.style.toTextStyle(),
        color = decoded.color?.toArchitectColor() ?: Color.Unspecified,
        textAlign = decoded.align.toTextAlign(),
        modifier = if (needsFullWidth) Modifier.fillMaxWidth() else Modifier,
    )
}

private fun String.toTextAlign(): TextAlign = when (this.uppercase()) {
    "CENTER" -> TextAlign.Center
    "END", "RIGHT" -> TextAlign.End
    else -> TextAlign.Start
}

private fun String.toTextStyle(): TextStyle = when (this.uppercase()) {
    "DISPLAY" -> ArchitectTypography.Display
    "HEADING", "HEADING1" -> ArchitectTypography.Heading1
    "HEADING2" -> ArchitectTypography.Heading2
    "HEADING3" -> ArchitectTypography.Heading3
    "CAPTION" -> ArchitectTypography.Caption
    "LABEL" -> ArchitectTypography.Label
    else -> ArchitectTypography.Body
}

internal fun String.toArchitectColor(): Color = when (this) {
    "NavyPrimary" -> ArchitectColors.NavyPrimary
    "NavySecondary" -> ArchitectColors.NavySecondary
    "GoldAccent" -> ArchitectColors.GoldAccent
    "White" -> ArchitectColors.White
    "OffWhite" -> ArchitectColors.OffWhite
    "LightGray" -> ArchitectColors.LightGray
    "MediumGray" -> ArchitectColors.MediumGray
    "DarkGray" -> ArchitectColors.DarkGray
    "Success" -> ArchitectColors.Success
    "Error" -> ArchitectColors.Error
    "Info" -> ArchitectColors.Info
    "TealAccent" -> ArchitectColors.TealAccent
    else -> Color.Unspecified
}
