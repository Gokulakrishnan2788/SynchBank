package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the SPACER SDUI component. */
@Serializable
data class SpacerComponentProps(val height: Int = 16)

/**
 * Renders a vertical SPACER SDUI component.
 *
 * @param props Raw JSON props decoded into [SpacerComponentProps].
 * @param onAction Unused — provided for uniform composable signature.
 */
@Composable
fun SpacerComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json.decodeFromJsonElement<SpacerComponentProps>(props)
    }.getOrDefault(SpacerComponentProps())

    Spacer(modifier = Modifier.height(decoded.height.dp))
}
