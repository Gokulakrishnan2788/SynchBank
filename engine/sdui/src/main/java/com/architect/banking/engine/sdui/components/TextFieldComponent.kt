package com.architect.banking.engine.sdui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.architect.banking.core.ui.components.ArchInputType
import com.architect.banking.core.ui.components.ArchTextField
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
)

/**
 * Renders a TEXT_FIELD SDUI component using the design-system [ArchTextField].
 * Internal state is managed locally — the ViewModel receives the value via action.
 *
 * @param props Raw JSON props decoded into [TextFieldComponentProps].
 * @param onAction Unused at field level — called via button action with SUBMIT_FORM.
 */
@Composable
fun TextFieldComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json.decodeFromJsonElement<TextFieldComponentProps>(props)
    }.getOrDefault(TextFieldComponentProps())

    var value by rememberSaveable { mutableStateOf("") }

    ArchTextField(
        label = decoded.label,
        value = value,
        onValueChange = { value = it },
        hint = decoded.hint,
        inputType = decoded.inputType.toArchInputType(),
    )
}

private fun String.toArchInputType(): ArchInputType = when (this.uppercase()) {
    "EMAIL" -> ArchInputType.EMAIL
    "PASSWORD" -> ArchInputType.PASSWORD
    "PHONE" -> ArchInputType.PHONE
    "NUMBER" -> ArchInputType.NUMBER
    else -> ArchInputType.TEXT
}
