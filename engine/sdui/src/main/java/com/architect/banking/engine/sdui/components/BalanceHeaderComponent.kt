package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTypography
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class BalanceHeaderProps(
    val label: String = "TOTAL COMBINED BALANCE",
    val amount: String = "",
    val change: String = "",
    val changeColor: String = "Success",
)

@Composable
fun BalanceHeaderComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<BalanceHeaderProps>(props)
    }.getOrDefault(BalanceHeaderProps())

    val badgeBg = when (decoded.changeColor.lowercase()) {
        "success" -> ArchitectColors.Success
        "tealaccent", "teal" -> ArchitectColors.TealAccent
        "error" -> ArchitectColors.Error
        else -> ArchitectColors.Success
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = decoded.label,
            style = ArchitectTypography.Label,
            color = ArchitectColors.MediumGray,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = decoded.amount,
            style = ArchitectTypography.Display.copy(fontSize = 38.sp),
            color = ArchitectColors.NavyPrimary,
        )
        if (decoded.change.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = decoded.change,
                    style = ArchitectTypography.Label.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(badgeBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                )
            }
        }
    }
}
