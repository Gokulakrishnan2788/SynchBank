package com.architect.banking.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTheme

/**
 * Keyboard/input type matching the SDUI TEXT_FIELD inputType values.
 */
enum class ArchInputType {
    TEXT,
    EMAIL,
    PASSWORD,
    PHONE,
    NUMBER,
}

/**
 * Visual style variants for [ArchTextField].
 */
enum class ArchTextFieldVariant {
    /** Outlined border style (default). */
    OUTLINED,
    /** Filled background style with no border. */
    FILLED,
}

/**
 * Design-system text field matching the SDUI TEXT_FIELD component.
 *
 * @param label Field label shown above the input (only used for OUTLINED variant).
 * @param value Current text value.
 * @param onValueChange Callback invoked when text changes.
 * @param modifier Optional layout modifier.
 * @param hint Placeholder text shown when [value] is empty.
 * @param inputType Determines keyboard type and visual transformation.
 * @param variant Visual style — [ArchTextFieldVariant.OUTLINED] or [ArchTextFieldVariant.FILLED].
 * @param error Optional error message shown below the field.
 * @param enabled Whether the field is interactive.
 * @param imeAction IME action (Next, Done, etc.).
 */
@Composable
fun ArchTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    inputType: ArchInputType = ArchInputType.TEXT,
    variant: ArchTextFieldVariant = ArchTextFieldVariant.OUTLINED,
    error: String? = null,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Next,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val keyboardOptions = KeyboardOptions(
        keyboardType = inputType.toKeyboardType(),
        imeAction = imeAction,
    )

    val visualTransformation = if (inputType == ArchInputType.PASSWORD && !passwordVisible) {
        PasswordVisualTransformation()
    } else {
        VisualTransformation.None
    }

    val trailingIcon: (@Composable () -> Unit)? = if (inputType == ArchInputType.PASSWORD) {
        {
            val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = icon, contentDescription = null, tint = ArchitectColors.MediumGray)
            }
        }
    } else null

    when (variant) {
        ArchTextFieldVariant.FILLED -> {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = if (hint.isNotEmpty()) {
                    { Text(text = hint, style = MaterialTheme.typography.bodyMedium, color = ArchitectColors.MediumGray) }
                } else null,
                modifier = modifier.fillMaxWidth(),
                enabled = enabled,
                isError = error != null,
                supportingText = error?.let { { Text(text = it, color = ArchitectColors.Error) } },
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                trailingIcon = trailingIcon,
                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = ArchitectColors.WarmSurface,
                    unfocusedContainerColor = ArchitectColors.WarmSurface,
                    disabledContainerColor = ArchitectColors.WarmSurface,
                    errorContainerColor = ArchitectColors.WarmSurface,
                    focusedIndicatorColor = ArchitectColors.NavyPrimary,
                    unfocusedIndicatorColor = ArchitectColors.NavySecondary,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = ArchitectColors.Error,
                    focusedTextColor = ArchitectColors.NavyPrimary,
                    unfocusedTextColor = ArchitectColors.DarkGray,
                    cursorColor = ArchitectColors.NavyPrimary,
                ),
                singleLine = true,
            )
        }
        ArchTextFieldVariant.OUTLINED -> {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                label = if (label.isNotEmpty()) { { Text(text = label) } } else null,
                placeholder = if (hint.isNotEmpty()) {
                    { Text(text = hint, style = MaterialTheme.typography.bodyMedium) }
                } else null,
                modifier = modifier.fillMaxWidth(),
                enabled = enabled,
                isError = error != null,
                supportingText = error?.let { { Text(text = it) } },
                keyboardOptions = keyboardOptions,
                visualTransformation = visualTransformation,
                trailingIcon = trailingIcon,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ArchitectColors.NavyPrimary,
                    focusedLabelColor = ArchitectColors.NavyPrimary,
                    cursorColor = ArchitectColors.NavyPrimary,
                    errorBorderColor = ArchitectColors.Error,
                    errorLabelColor = ArchitectColors.Error,
                ),
                singleLine = true,
            )
        }
    }
}

private fun ArchInputType.toKeyboardType(): KeyboardType = when (this) {
    ArchInputType.TEXT -> KeyboardType.Text
    ArchInputType.EMAIL -> KeyboardType.Email
    ArchInputType.PASSWORD -> KeyboardType.Password
    ArchInputType.PHONE -> KeyboardType.Phone
    ArchInputType.NUMBER -> KeyboardType.Number
}

@Preview(showBackground = true)
@Composable
private fun ArchTextFieldPreview() {
    ArchitectTheme {
        ArchTextField(
            label = "Username",
            value = "",
            onValueChange = {},
            hint = "Institutional ID or Email",
            inputType = ArchInputType.EMAIL,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ArchTextFieldErrorPreview() {
    ArchitectTheme {
        ArchTextField(
            label = "Password",
            value = "pass",
            onValueChange = {},
            inputType = ArchInputType.PASSWORD,
            error = "Password must be at least 8 characters",
        )
    }
}
