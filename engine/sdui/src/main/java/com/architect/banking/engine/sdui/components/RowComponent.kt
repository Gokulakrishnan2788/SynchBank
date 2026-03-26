package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.architect.banking.engine.sdui.model.ComponentModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the ROW SDUI component. */
@Serializable
data class RowComponentProps(
    val horizontalArrangement: String = "START",
    val verticalAlignment: String = "CENTER",
    val spacing: Int = 0,
    val padding: PaddingProps? = null,
)

@Composable
fun RowComponent(
    props: JsonObject,
    children: List<ComponentModel>,
    onAction: (String) -> Unit,
    childRenderer: @Composable (ComponentModel, (String) -> Unit) -> Unit,
) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }
            .decodeFromJsonElement<RowComponentProps>(props)
    }.getOrDefault(RowComponentProps())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = decoded.padding?.horizontal?.dp ?: 0.dp,
                vertical = decoded.padding?.vertical?.dp ?: 0.dp
            ),
        horizontalArrangement = decoded.horizontalArrangement.toHorizontalArrangement(decoded.spacing),
        verticalAlignment = decoded.verticalAlignment.toVerticalAlignment(),
    ) {
        children.forEach { child ->
            if (child.visible) {
                childRenderer(child, onAction)
            }
        }
    }
}

private fun String.toHorizontalArrangement(spacing: Int): Arrangement.Horizontal {
    return when (this.uppercase()) {
        "START" -> Arrangement.spacedBy(spacing.dp, Alignment.Start)
        "CENTER" -> Arrangement.spacedBy(spacing.dp, Alignment.CenterHorizontally)
        "END" -> Arrangement.spacedBy(spacing.dp, Alignment.End)
        "SPACE_BETWEEN" -> Arrangement.SpaceBetween
        "SPACE_AROUND" -> Arrangement.SpaceAround
        "SPACE_EVENLY" -> Arrangement.SpaceEvenly
        else -> Arrangement.spacedBy(spacing.dp, Alignment.Start)
    }
}

private fun String.toVerticalAlignment(): Alignment.Vertical {
    return when (this.uppercase()) {
        "TOP" -> Alignment.Top
        "BOTTOM" -> Alignment.Bottom
        "CENTER" -> Alignment.CenterVertically
        else -> Alignment.CenterVertically
    }
}