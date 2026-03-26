package com.architect.banking.engine.sdui.components

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the DIVIDER SDUI component. */
@Serializable
data class DividerComponentProps(
    val thickness: Int = 1,
    val color: String? = null,
)

/**
 * Renders a horizontal DIVIDER SDUI component.
 *
 * @param props Raw JSON props decoded into [DividerComponentProps].
 * @param onAction Unused — provided for uniform composable signature.
 */
@Composable
fun DividerComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json.decodeFromJsonElement<DividerComponentProps>(props)
    }.getOrDefault(DividerComponentProps())

    HorizontalDivider(
        thickness = decoded.thickness.dp,
        color = decoded.color?.toArchitectColor() ?: Color.LightGray,
    )
}
