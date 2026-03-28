package com.architect.banking.engine.sdui.components

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class BiometricRowComponentProps(
    val options: List<String> = emptyList(),
    val label: String = "AUTHENTICATION",
)

@Composable
fun BiometricRowComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<BiometricRowComponentProps>(props)
    }.getOrDefault(BiometricRowComponentProps())

    val context = LocalContext.current
    val biometricEnabled = context
        .getSharedPreferences("architect_prefs", Context.MODE_PRIVATE)
        .getBoolean("biometric_login_enabled", true)

    val iconTint = if (biometricEnabled) ArchitectColors.NavyPrimary else ArchitectColors.MediumGray
    val cardColor = if (biometricEnabled) ArchitectColors.White else ArchitectColors.WarmSurface

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if ("FINGERPRINT" in decoded.options) {
            Surface(
                onClick = {
                    if (biometricEnabled) {
                        onAction("BIOMETRIC_FINGERPRINT")
                    } else {
                        Toast.makeText(context, "Biometric login disabled", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.size(72.dp),
                shape = RoundedCornerShape(16.dp),
                color = cardColor,
                shadowElevation = if (biometricEnabled) 2.dp else 0.dp,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Fingerprint,
                        contentDescription = "Fingerprint authentication",
                        modifier = Modifier.size(32.dp),
                        tint = iconTint,
                    )
                }
            }
        }
        if ("FACE_ID" in decoded.options) {
            Surface(
                onClick = {
                    if (biometricEnabled) {
                        onAction("BIOMETRIC_FACE_ID")
                    } else {
                        Toast.makeText(context, "Biometric login disabled", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.size(72.dp),
                shape = RoundedCornerShape(16.dp),
                color = cardColor,
                shadowElevation = if (biometricEnabled) 2.dp else 0.dp,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Face,
                        contentDescription = "Face ID authentication",
                        modifier = Modifier.size(32.dp),
                        tint = iconTint,
                    )
                }
            }
        }
    }
}
