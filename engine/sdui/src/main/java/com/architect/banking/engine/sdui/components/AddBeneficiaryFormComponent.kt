package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.components.ArchInputType
import com.architect.banking.core.ui.components.ArchTextField
import com.architect.banking.core.ui.components.ArchTextFieldVariant
import com.architect.banking.core.ui.theme.ArchitectColors
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class FormField(
    val id: String = "",
    val label: String = "",
    val hint: String = "",
    val helperText: String? = null,
    val inputType: String = "TEXT",
    val fieldType: String = "TEXT",
    val options: List<String> = emptyList(),
)

@Serializable
data class AddBeneficiaryFormProps(
    val fields: List<FormField> = emptyList(),
)

@Composable
fun AddBeneficiaryFormComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<AddBeneficiaryFormProps>(props)
    }.getOrDefault(AddBeneficiaryFormProps())

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ArchitectColors.FormCardBg,
        shadowElevation = 1.dp,
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
            decoded.fields.forEachIndexed { index, field ->
                if (index > 0) Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = field.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = ArchitectColors.MediumGray,
                    modifier = Modifier.padding(bottom = 6.dp),
                )

                if (field.fieldType.uppercase() == "DROPDOWN") {
                    BankDropdown(
                        field = field,
                        onAction = onAction,
                    )
                } else {
                    var value by rememberSaveable(field.id) { mutableStateOf("") }
                    val inputType = when (field.inputType.uppercase()) {
                        "NUMBER" -> ArchInputType.NUMBER
                        "EMAIL" -> ArchInputType.EMAIL
                        "PASSWORD" -> ArchInputType.PASSWORD
                        "PHONE" -> ArchInputType.PHONE
                        else -> ArchInputType.TEXT
                    }
                    ArchTextField(
                        label = "",
                        value = value,
                        onValueChange = { v ->
                            value = v
                            onAction("FIELD_CHANGE:${field.id}:$v")
                        },
                        hint = field.hint,
                        inputType = inputType,
                        variant = ArchTextFieldVariant.FILLED,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                if (!field.helperText.isNullOrBlank()) {
                    Text(
                        text = field.helperText,
                        style = MaterialTheme.typography.labelSmall,
                        color = ArchitectColors.MediumGray,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun BankDropdown(field: FormField, onAction: (String) -> Unit) {
    var selected by rememberSaveable(field.id) { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
            color = ArchitectColors.WarmSurface,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = selected.ifEmpty { field.hint },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selected.isEmpty()) ArchitectColors.MediumGray else ArchitectColors.NavyPrimary,
                    modifier = Modifier.weight(1f),
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = ArchitectColors.MediumGray,
                )
            }
        }
        HorizontalDivider(
            color = if (expanded) ArchitectColors.NavyPrimary else ArchitectColors.MediumGray.copy(alpha = 0.4f),
            thickness = 1.dp,
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.85f),
        ) {
            field.options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ArchitectColors.NavyPrimary,
                        )
                    },
                    onClick = {
                        selected = option
                        expanded = false
                        onAction("FIELD_CHANGE:${field.id}:$option")
                    },
                )
            }
        }
    }
}
