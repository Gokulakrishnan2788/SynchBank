package com.architect.banking.engine.sdui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTypography
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class SettingsRow(
    val id: String = "",
    val icon: String = "",
    val title: String = "",
    val subtitle: String? = null,
    val rightType: String = "CHEVRON", // CHEVRON | TOGGLE | VALUE
    val rightValue: String? = null,
    val defaultToggleOn: Boolean = false,
    val actionId: String = "",
)

@Serializable
data class ProfileSettingsCardProps(
    val rows: List<SettingsRow> = emptyList(),
)

@Composable
fun ProfileSettingsCardComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<ProfileSettingsCardProps>(props)
    }.getOrDefault(ProfileSettingsCardProps())

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ArchitectColors.White,
        shadowElevation = 2.dp,
    ) {
        Column(modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)) {
            decoded.rows.forEachIndexed { index, row ->
                if (index > 0) {
                    HorizontalDivider(
                        color = ArchitectColors.LightGray,
                        thickness = 1.dp,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                }
                SettingsRowItem(row = row, onAction = onAction)
            }
        }
    }
}

@Composable
private fun SettingsRowItem(row: SettingsRow, onAction: (String) -> Unit) {
    val context = LocalContext.current

    val initialToggle = if (row.id == "biometric_login") {
        context.getSharedPreferences("architect_prefs", Context.MODE_PRIVATE)
            .getBoolean("biometric_login_enabled", true)
    } else {
        row.defaultToggleOn
    }

    var toggleState by rememberSaveable(row.id) { mutableStateOf(initialToggle) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = row.rightType != "TOGGLE") {
                if (row.actionId.isNotEmpty()) onAction(row.actionId)
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Icon box
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(ArchitectColors.TealAccent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = resolveSettingsIcon(row.icon),
                contentDescription = null,
                tint = ArchitectColors.TealAccent,
                modifier = Modifier.size(20.dp),
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = row.title,
                style = ArchitectTypography.Body.copy(fontWeight = FontWeight.Medium),
                color = ArchitectColors.NavyPrimary,
            )
            if (!row.subtitle.isNullOrBlank()) {
                Text(
                    text = row.subtitle,
                    style = ArchitectTypography.Caption,
                    color = ArchitectColors.MediumGray,
                )
            }
        }

        // Right element
        when (row.rightType.uppercase()) {
            "TOGGLE" -> {
                Switch(
                    checked = toggleState,
                    onCheckedChange = { checked ->
                        toggleState = checked
                        if (row.id == "biometric_login") {
                            context.getSharedPreferences("architect_prefs", Context.MODE_PRIVATE)
                                .edit()
                                .putBoolean("biometric_login_enabled", checked)
                                .apply()
                        }
                        if (row.actionId.isNotEmpty()) onAction("${row.actionId}:$checked")
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = ArchitectColors.White,
                        checkedTrackColor = ArchitectColors.NavyPrimary,
                    ),
                )
            }
            "VALUE" -> {
                val displayValue = if (row.id == "language") {
                    val locale = java.util.Locale.getDefault()
                    "${locale.displayLanguage} (${locale.country})"
                } else {
                    row.rightValue ?: ""
                }
                Text(
                    text = displayValue,
                    style = ArchitectTypography.BodySmall,
                    color = ArchitectColors.MediumGray,
                )
            }
            else -> { // CHEVRON
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = ArchitectColors.MediumGray,
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

private fun resolveSettingsIcon(name: String): ImageVector = when (name.lowercase()) {
    "lock", "password", "change_password" -> Icons.Default.Lock
    "fingerprint", "biometric" -> Icons.Default.Fingerprint
    "notifications", "bell" -> Icons.Default.Notifications
    "appearance", "theme", "dark_mode" -> Icons.Default.DarkMode
    "language", "globe" -> Icons.Default.Language
    else -> Icons.Default.ChevronRight
}
