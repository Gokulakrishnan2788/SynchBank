package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectSpacing
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the BIOMETRIC_ROW SDUI component. */
@Serializable
data class BiometricRowComponentProps(
    val options: List<String> = emptyList(),
    val label: String = "AUTHENTICATION",
)

/**
 * Renders a BIOMETRIC_ROW SDUI component with fingerprint and/or face icons.
 *
 * @param props Raw JSON props decoded into [BiometricRowComponentProps].
 * @param onAction Called with action ID when a biometric icon is tapped.
 */
@Composable
fun BiometricRowComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json.decodeFromJsonElement<BiometricRowComponentProps>(props)
    }.getOrDefault(BiometricRowComponentProps())

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(ArchitectSpacing.MD, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = decoded.label,
            style = MaterialTheme.typography.labelSmall,
            color = ArchitectColors.MediumGray,
        )
        if ("FINGERPRINT" in decoded.options) {
            IconButton(onClick = { onAction("BIOMETRIC_FINGERPRINT") }) {
                Icon(
                    imageVector = Icons.Default.Fingerprint,
                    contentDescription = "Fingerprint authentication",
                    tint = ArchitectColors.NavyPrimary,
                )
            }
        }
        if ("FACE_ID" in decoded.options) {
            IconButton(onClick = { onAction("BIOMETRIC_FACE_ID") }) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = "Face ID authentication",
                    tint = ArchitectColors.NavyPrimary,
                )
            }
        }
    }
}
