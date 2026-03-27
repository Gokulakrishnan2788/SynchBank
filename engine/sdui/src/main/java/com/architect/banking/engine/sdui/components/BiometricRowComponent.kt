package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
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
 * Renders a BIOMETRIC_ROW SDUI component as white card buttons with fingerprint/face icons.
 *
 * @param props Raw JSON props decoded into [BiometricRowComponentProps].
 * @param onAction Called with action ID when a biometric card is tapped.
 */
@Composable
fun BiometricRowComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json.decodeFromJsonElement<BiometricRowComponentProps>(props)
    }.getOrDefault(BiometricRowComponentProps())

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if ("FINGERPRINT" in decoded.options) {
            Surface(
                onClick = { onAction("BIOMETRIC_FINGERPRINT") },
                modifier = Modifier.size(72.dp),
                shape = RoundedCornerShape(16.dp),
                color = ArchitectColors.White,
                shadowElevation = 2.dp,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "Fingerprint authentication",
                        modifier = Modifier.size(32.dp),
                        tint = ArchitectColors.NavyPrimary,
                    )
                }
            }
        }
        if ("FACE_ID" in decoded.options) {
            Surface(
                onClick = { onAction("BIOMETRIC_FACE_ID") },
                modifier = Modifier.size(72.dp),
                shape = RoundedCornerShape(16.dp),
                color = ArchitectColors.White,
                shadowElevation = 2.dp,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Face ID authentication",
                        modifier = Modifier.size(32.dp),
                        tint = ArchitectColors.NavyPrimary,
                    )
                }
            }
        }
    }
}
