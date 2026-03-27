package com.architect.banking.engine.sdui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.architect.banking.core.ui.components.ArchInputType
import com.architect.banking.core.ui.components.ArchTextField
import com.architect.banking.core.ui.components.ArchTextFieldVariant
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the TEXT_FIELD SDUI component. */
@Serializable
data class TextFieldComponentProps(
    val label: String = "",
    val hint: String = "",
    val inputType: String = "TEXT",
    val required: Boolean = false,
    val variant: String = "OUTLINED",
)

/**
 * Renders a TEXT_FIELD SDUI component using the design-system [ArchTextField].
 * Value changes are propagated up via [onAction] as "FIELD_CHANGE:<fieldId>:<value>" so
 * the ViewModel can update its state on every keystroke.
 *
 * @param fieldId The component's SDUI id — used to identify the field in change events.
 * @param props Raw JSON props decoded into [TextFieldComponentProps].
 * @param onAction Called with SUBMIT_FORM (button tap) or FIELD_CHANGE:fieldId:value (text change).
 */
@Composable
fun TextFieldComponent(fieldId: String, props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json.decodeFromJsonElement<TextFieldComponentProps>(props)
    }.getOrDefault(TextFieldComponentProps())

    var value by rememberSaveable { mutableStateOf("") }

    ArchTextField(
        label = decoded.label,
        value = value,
        onValueChange = {
            value = it
            onAction("FIELD_CHANGE:$fieldId:$it")
        },
        hint = decoded.hint,
        inputType = decoded.inputType.toArchInputType(),
        variant = decoded.variant.toArchTextFieldVariant(),
    )
}

private fun String.toArchInputType(): ArchInputType = when (this.uppercase()) {
    "EMAIL" -> ArchInputType.EMAIL
    "PASSWORD" -> ArchInputType.PASSWORD
    "PHONE" -> ArchInputType.PHONE
    "NUMBER" -> ArchInputType.NUMBER
    else -> ArchInputType.TEXT
}

private fun String.toArchTextFieldVariant(): ArchTextFieldVariant = when (this.uppercase()) {
    "FILLED" -> ArchTextFieldVariant.FILLED
    else -> ArchTextFieldVariant.OUTLINED
}
