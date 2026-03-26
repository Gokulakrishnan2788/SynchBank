package com.architect.banking.engine.sdui.components

import androidx.compose.runtime.Composable
import com.architect.banking.core.ui.components.ArchCard
import com.architect.banking.engine.sdui.model.ComponentModel
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the CARD SDUI component. */
@Serializable
data class CardComponentProps(
    val elevation: Float = 2f,
    val children: List<ComponentModel> = emptyList(),
)

/**
 * Renders a CARD SDUI component containing nested child components.
 * Uses the design-system [ArchCard] as the container.
 *
 * @param props Raw JSON props decoded into [CardComponentProps].
 * @param onAction Forwarded to each child component.
 * @param childRenderer Lambda that renders a [ComponentModel] — injected by [SDUIRenderer].
 */
@Composable
fun CardComponent(
    props: JsonObject,
    onAction: (String) -> Unit,
    childRenderer: @Composable (ComponentModel, (String) -> Unit) -> Unit,
) {
    val decoded = runCatching {
        Json.decodeFromJsonElement<CardComponentProps>(props)
    }.getOrDefault(CardComponentProps())

    ArchCard(elevation = decoded.elevation) {
        decoded.children.forEach { child ->
            if (child.visible) {
                childRenderer(child, onAction)
            }
        }
    }
}
