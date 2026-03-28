package com.architect.banking.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTheme

/**
 * Button style variants matching the SDUI [ButtonComponentProps.style] values.
 */
enum class ArchButtonStyle {
    PRIMARY,
    SECONDARY,
    GHOST,
    DESTRUCTIVE,
}

/**
 * Design-system button that maps to SDUI BUTTON component.
 *
 * @param label Text displayed inside the button.
 * @param onClick Action triggered on tap.
 * @param modifier Optional layout modifier.
 * @param style Visual style of the button. Defaults to [ArchButtonStyle.PRIMARY].
 * @param containerColor Overrides the button background color when provided.
 * @param loading When true, replaces label with a spinner and disables interaction.
 * @param enabled Whether the button is interactive.
 */
@Composable
fun ArchButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ArchButtonStyle = ArchButtonStyle.PRIMARY,
    containerColor: Color? = null,
    loading: Boolean = false,
    enabled: Boolean = true,
) {
    val buttonModifier = modifier
        .fillMaxWidth()
        .height(52.dp)

    when (style) {
        ArchButtonStyle.PRIMARY -> PrimaryButton(label, onClick, buttonModifier, containerColor, loading, enabled)
        ArchButtonStyle.SECONDARY -> SecondaryButton(label, onClick, buttonModifier, loading, enabled)
        ArchButtonStyle.GHOST -> GhostButton(label, onClick, buttonModifier, enabled)
        ArchButtonStyle.DESTRUCTIVE -> DestructiveButton(label, onClick, buttonModifier, loading, enabled)
    }
}

@Composable
private fun PrimaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier,
    containerColor: Color?,
    loading: Boolean,
    enabled: Boolean,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !loading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor ?: ArchitectColors.NavyPrimary,
            contentColor = ArchitectColors.White,
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(color = ArchitectColors.White, strokeWidth = 2.dp)
        } else {
            Text(text = label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun SecondaryButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier,
    loading: Boolean,
    enabled: Boolean,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !loading,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = ArchitectColors.NavyPrimary,
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(color = ArchitectColors.NavyPrimary, strokeWidth = 2.dp)
        } else {
            Text(text = label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun GhostButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier,
    enabled: Boolean,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = ButtonDefaults.textButtonColors(
            contentColor = ArchitectColors.GoldAccent,
        ),
    ) {
        Text(text = label, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
private fun DestructiveButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier,
    loading: Boolean,
    enabled: Boolean,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !loading,
        colors = ButtonDefaults.buttonColors(
            containerColor = ArchitectColors.Error,
            contentColor = Color.White,
        ),
    ) {
        if (loading) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp)
        } else {
            Text(text = label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArchButtonPrimaryPreview() {
    ArchitectTheme {
        ArchButton(label = "Login", onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ArchButtonLoadingPreview() {
    ArchitectTheme {
        ArchButton(label = "Login", onClick = {}, loading = true)
    }
}

@Preview(showBackground = true)
@Composable
private fun ArchButtonSecondaryPreview() {
    ArchitectTheme {
        ArchButton(label = "Cancel", onClick = {}, style = ArchButtonStyle.SECONDARY)
    }
}
