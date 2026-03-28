package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.architect.banking.core.ui.theme.ArchitectColors
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class SectionHeaderRowProps(
    val title: String = "",
    val trailingLabel: String = "",
    val trailingColor: String = "TealAccent",
    val trailingAction: String = "",
)

@Composable
fun SectionHeaderRowComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<SectionHeaderRowProps>(props)
    }.getOrDefault(SectionHeaderRowProps())

    val trailingColor = when (decoded.trailingColor.lowercase()) {
        "tealaccent", "teal" -> ArchitectColors.TealAccent
        "navyprimary" -> ArchitectColors.NavyPrimary
        "goldaccent" -> ArchitectColors.GoldAccent
        "info" -> ArchitectColors.Info
        else -> ArchitectColors.TealAccent
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = decoded.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = ArchitectColors.NavyPrimary,
            modifier = Modifier.weight(1f),
        )
        if (decoded.trailingLabel.isNotEmpty()) {
            Text(
                text = decoded.trailingLabel,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                color = trailingColor,
                modifier = Modifier.clickable {
                    if (decoded.trailingAction.isNotEmpty()) onAction(decoded.trailingAction)
                },
            )
        }
    }
}
