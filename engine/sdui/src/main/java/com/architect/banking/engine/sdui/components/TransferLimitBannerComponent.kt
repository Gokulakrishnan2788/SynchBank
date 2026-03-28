package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.architect.banking.core.ui.theme.ArchitectColors
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class TransferLimitBannerProps(
    val label: String = "Transfer Limit",
    val amount: String = "",
    val suffix: String = "DAILY REMAINING",
    val bgColor: String = "TealAccent",
)

@Composable
fun TransferLimitBannerComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<TransferLimitBannerProps>(props)
    }.getOrDefault(TransferLimitBannerProps())

    val bg = when (decoded.bgColor.lowercase()) {
        "tealaccent", "teal" -> ArchitectColors.TealAccent
        "navyprimary" -> ArchitectColors.NavyPrimary
        "goldaccent" -> ArchitectColors.GoldAccent
        "success" -> ArchitectColors.Success
        else -> ArchitectColors.TealAccent
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = bg,
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
            Text(
                text = decoded.label,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = ArchitectColors.White,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = decoded.amount,
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = ArchitectColors.White,
                )
                Text(
                    text = "  ${decoded.suffix}",
                    style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 0.5.sp),
                    color = ArchitectColors.White.copy(alpha = 0.8f),
                    modifier = Modifier.padding(bottom = 4.dp),
                )
            }
        }
    }
}
