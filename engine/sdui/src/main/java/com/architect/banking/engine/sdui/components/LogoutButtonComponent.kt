package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTypography
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class LogoutButtonProps(
    val label: String = "Log Out",
)

@Composable
fun LogoutButtonComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<LogoutButtonProps>(props)
    }.getOrDefault(LogoutButtonProps())

    OutlinedButton(
        onClick = { onAction("") },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, ArchitectColors.LightGray),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = ArchitectColors.Error,
        ),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = null,
                tint = ArchitectColors.Error,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = decoded.label,
                style = ArchitectTypography.ButtonText,
                color = ArchitectColors.Error,
            )
        }
    }
}
