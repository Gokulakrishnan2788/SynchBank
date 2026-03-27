package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the DIVIDER SDUI component. */
@Serializable
data class DividerComponentProps(
    val thickness: Int = 1,
    val color: String? = null,
    val label: String? = null,
)

/**
 * Renders a DIVIDER SDUI component.
 * When [DividerComponentProps.label] is provided, renders a labeled divider
 * with the text centered between two horizontal lines.
 *
 * @param props Raw JSON props decoded into [DividerComponentProps].
 * @param onAction Unused — provided for uniform composable signature.
 */
@Composable
fun DividerComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json.decodeFromJsonElement<DividerComponentProps>(props)
    }.getOrDefault(DividerComponentProps())

    val lineColor = decoded.color?.toArchitectColor() ?: ArchitectColors.LightGray

    if (decoded.label != null) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = decoded.thickness.dp,
                color = lineColor,
            )
            Text(
                text = decoded.label,
                modifier = Modifier.padding(horizontal = 12.dp),
                style = MaterialTheme.typography.labelSmall,
                color = ArchitectColors.MediumGray,
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = decoded.thickness.dp,
                color = lineColor,
            )
        }
    } else {
        HorizontalDivider(
            thickness = decoded.thickness.dp,
            color = lineColor,
        )
    }
}
