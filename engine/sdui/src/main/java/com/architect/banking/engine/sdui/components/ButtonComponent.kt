package com.architect.banking.engine.sdui.components

import androidx.compose.runtime.Composable
import com.architect.banking.core.ui.components.ArchButton
import com.architect.banking.core.ui.components.ArchButtonStyle
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the BUTTON SDUI component. */
@Serializable
data class ButtonComponentProps(
    val label: String = "",
    val style: String = "PRIMARY",
    val loading: Boolean = false,
)

/**
 * Renders a BUTTON SDUI component using the design-system [ArchButton].
 *
 * @param props Raw JSON props decoded into [ButtonComponentProps].
 * @param onAction Called with the component's action ID when tapped.
 */
@Composable
fun ButtonComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json.decodeFromJsonElement<ButtonComponentProps>(props)
    }.getOrDefault(ButtonComponentProps())

    ArchButton(
        label = decoded.label,
        onClick = { onAction("") },
        style = decoded.style.toArchButtonStyle(),
        loading = decoded.loading,
    )
}

private fun String.toArchButtonStyle(): ArchButtonStyle = when (this.uppercase()) {
    "SECONDARY" -> ArchButtonStyle.SECONDARY
    "GHOST" -> ArchButtonStyle.GHOST
    "DESTRUCTIVE" -> ArchButtonStyle.DESTRUCTIVE
    else -> ArchButtonStyle.PRIMARY
}
