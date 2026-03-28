package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTypography
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class InfoRow(
    val label: String = "",
    val value: String = "",
)

@Serializable
data class ProfileInfoCardProps(
    val rows: List<InfoRow> = emptyList(),
)

@Composable
fun ProfileInfoCardComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<ProfileInfoCardProps>(props)
    }.getOrDefault(ProfileInfoCardProps())

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ArchitectColors.White,
        shadowElevation = 2.dp,
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)) {
            decoded.rows.forEachIndexed { index, row ->
                if (index > 0) {
                    HorizontalDivider(
                        color = ArchitectColors.LightGray,
                        thickness = 1.dp,
                    )
                }
                Column(modifier = Modifier.padding(vertical = 16.dp)) {
                    Text(
                        text = row.label,
                        style = ArchitectTypography.Label,
                        color = ArchitectColors.MediumGray,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = row.value,
                        style = ArchitectTypography.Body,
                        color = ArchitectColors.NavyPrimary,
                    )
                }
            }
        }
    }
}
