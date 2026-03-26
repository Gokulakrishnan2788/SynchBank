package com.architect.banking.engine.sdui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTypography
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the TEXT SDUI component. */
@Serializable
data class TextComponentProps(
    val text: String = "",
    val style: String = "BODY",
    val color: String? = null,
)

/**
 * Renders a TEXT SDUI component.
 *
 * @param props Raw JSON props decoded into [TextComponentProps].
 * @param onAction Unused for text — provided for uniform composable signature.
 */
@Composable
fun TextComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        kotlinx.serialization.json.Json.decodeFromJsonElement<TextComponentProps>(props)
    }.getOrDefault(TextComponentProps())

    Text(
        text = decoded.text,
        style = decoded.style.toTextStyle(),
        color = decoded.color?.toArchitectColor() ?: Color.Unspecified,
    )
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
    else -> Color.Unspecified
}
