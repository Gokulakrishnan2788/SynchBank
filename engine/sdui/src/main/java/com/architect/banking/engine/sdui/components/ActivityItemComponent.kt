package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.LocalGroceryStore
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the ACTIVITY_ITEM SDUI component. */
@Serializable
data class ActivityItemProps(
    val iconName: String = "",
    val iconBackground: String = "LightGray",
    val iconTint: String = "White",
    val title: String = "",
    val subtitle: String = "",
    val amount: String = "",
    val amountColor: String = "NavyPrimary",
)

/**
 * Renders a single activity/transaction row with a rounded icon badge on the left,
 * title + subtitle stacked in the middle, and amount on the right.
 */
@Composable
fun ActivityItemComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }
            .decodeFromJsonElement<ActivityItemProps>(props)
    }.getOrDefault(ActivityItemProps())

    val bgColor = decoded.iconBackground.toActivityBgColor()
    val iconTint = decoded.iconTint.toActivityIconTint()
    val amountColor = decoded.amountColor.toActivityAmountColor()
    val icon = decoded.iconName.toActivityIcon()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Icon badge
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor),
            contentAlignment = Alignment.Center,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp),
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Title + subtitle
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = decoded.title,
                style = MaterialTheme.typography.bodyMedium,
                color = ArchitectColors.NavyPrimary,
            )
            Text(
                text = decoded.subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = ArchitectColors.MediumGray,
            )
        }

        // Amount
        Text(
            text = decoded.amount,
            style = MaterialTheme.typography.bodyMedium,
            color = amountColor,
        )
    }
}

private fun String.toActivityIcon(): ImageVector? = when (this.lowercase()) {
    "shopping_bag", "electronics", "shopping" -> Icons.Outlined.ShoppingBag
    "grocery", "store" -> Icons.Outlined.LocalGroceryStore
    "payments", "salary", "income", "money" -> Icons.Outlined.Payments
    "attach_money" -> Icons.Outlined.AttachMoney
    "restaurant", "dining", "food", "utensils" -> Icons.Outlined.Restaurant
    "car", "transport", "directions_car", "electric_car" -> Icons.Outlined.DirectionsCar
    else -> null
}

private fun String.toActivityBgColor(): Color = when (this.lowercase()) {
    "navyprimary", "navy_primary" -> ArchitectColors.NavyPrimary
    "success", "green" -> Color(0xFF1B5E20)
    "successlight", "success_light" -> Color(0xFFE8F5E9)
    "gold", "goldaccent", "gold_accent" -> ArchitectColors.GoldAccent
    "error", "red" -> ArchitectColors.Error
    "orange", "amber" -> Color(0xFFFFF3E0)
    "blue", "info" -> Color(0xFFE3F2FD)
    "lightgray", "light_gray" -> Color(0xFFF2F2F4)
    "teal" -> Color(0xFFE0F2F1)
    else -> Color(0xFFF2F2F4)
}

private fun String.toActivityIconTint(): Color = when (this.lowercase()) {
    "white" -> ArchitectColors.White
    "navyprimary", "navy_primary" -> ArchitectColors.NavyPrimary
    "success", "green" -> Color(0xFF2E7D32)
    "gold", "goldaccent" -> ArchitectColors.GoldAccent
    "error" -> ArchitectColors.Error
    "orange", "amber" -> Color(0xFFE65100)
    "blue", "info" -> Color(0xFF1565C0)
    "mediumgray", "gray" -> ArchitectColors.MediumGray
    else -> ArchitectColors.MediumGray
}

private fun String.toActivityAmountColor(): Color = when (this.lowercase()) {
    "success", "green" -> ArchitectColors.Success
    "error", "red" -> ArchitectColors.Error
    "navyprimary", "navy_primary" -> ArchitectColors.NavyPrimary
    "mediumgray", "gray" -> ArchitectColors.MediumGray
    else -> ArchitectColors.NavyPrimary
}
