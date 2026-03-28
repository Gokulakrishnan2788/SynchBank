package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the ICON SDUI component. */
@Serializable
data class IconComponentProps(
    val name: String = "",
    val size: Int = 40,
    val tint: String = "LightGray",
)

/**
 * Renders an ICON SDUI component using outlined Material Icons.
 */
@Composable
fun IconComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }
            .decodeFromJsonElement<IconComponentProps>(props)
    }.getOrDefault(IconComponentProps())

    val imageVector = decoded.name.toImageVector() ?: return
    val tintColor = decoded.tint.toArchitectColor().takeIf { it != androidx.compose.ui.graphics.Color.Unspecified }
        ?: ArchitectColors.LightGray

    Icon(
        imageVector = imageVector,
        contentDescription = null,
        tint = tintColor,
        modifier = Modifier.size(decoded.size.dp),
    )
}

private fun String.toImageVector(): ImageVector? = when (this.lowercase()) {
    "wallet" -> Icons.Outlined.AccountBalanceWallet
    "savings", "piggy" -> Icons.Outlined.Savings
    "card", "credit_card" -> Icons.Outlined.CreditCard
    "bank", "account_balance" -> Icons.Outlined.AccountBalance
    "trending_up", "growth" -> Icons.Outlined.TrendingUp
    else -> null
}
