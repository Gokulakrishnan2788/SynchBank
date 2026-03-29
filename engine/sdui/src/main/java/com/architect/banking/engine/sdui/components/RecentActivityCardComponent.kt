package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTypography
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class AccountActivityItem(
    val iconName: String = "",
    val iconBackground: String = "lightgray",
    val iconTint: String = "gray",
    val title: String = "",
    val subtitle: String = "",
    val amount: String = "",
    val amountColor: String = "NavyPrimary",
)

@Serializable
data class RecentActivityCardProps(
    val title: String = "Recent Activity",
    val subtitle: String = "",
    val viewAllLabel: String = "View Statement",
    val viewAllAction: String = "VIEW_STATEMENT",
    val items: List<AccountActivityItem> = emptyList(),
)

@Composable
fun RecentActivityCardComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<RecentActivityCardProps>(props)
    }.getOrDefault(RecentActivityCardProps())

    Column(modifier = Modifier.fillMaxWidth()) {
        // Section header — no card, floats on the background
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = decoded.title,
                    style = ArchitectTypography.Heading3.copy(fontWeight = FontWeight.Bold),
                    color = ArchitectColors.NavyPrimary,
                )
                if (decoded.subtitle.isNotEmpty()) {
                    Text(
                        text = decoded.subtitle,
                        style = ArchitectTypography.Caption,
                        color = ArchitectColors.MediumGray,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
            }
            Text(
                text = decoded.viewAllLabel,
                style = ArchitectTypography.Label.copy(fontWeight = FontWeight.SemiBold),
                color = ArchitectColors.TealAccent,
                modifier = Modifier
                    .padding(start = 8.dp, top = 2.dp)
                    .clickable { onAction(decoded.viewAllAction) },
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Each activity as its own individual white card
        decoded.items.forEachIndexed { index, item ->
            if (index > 0) Spacer(modifier = Modifier.height(10.dp))
            ActivityCard(item = item)
        }
    }
}

@Composable
private fun ActivityCard(item: AccountActivityItem) {
    val bgColor = when (item.iconBackground.lowercase()) {
        "success", "teal" -> ArchitectColors.TealAccent.copy(alpha = 0.15f)
        "orange" -> Color(0xFFF57C00).copy(alpha = 0.15f)
        "blue" -> Color(0xFF1565C0).copy(alpha = 0.15f)
        "navy", "navyprimary" -> ArchitectColors.NavyPrimary
        else -> ArchitectColors.WarmSurface
    }
    val tintColor = when (item.iconTint.lowercase()) {
        "white" -> Color.White
        "success", "teal" -> ArchitectColors.TealAccent
        "orange" -> Color(0xFFF57C00)
        "blue" -> Color(0xFF1565C0)
        "navy", "navyprimary" -> Color.White
        else -> ArchitectColors.MediumGray
    }
    val amtColor = when (item.amountColor.lowercase()) {
        "success" -> ArchitectColors.Success
        "tealaccent", "teal" -> ArchitectColors.TealAccent
        "error" -> ArchitectColors.Error
        else -> ArchitectColors.NavyPrimary
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ArchitectColors.White,
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = resolveActivityIcon(item.iconName),
                    contentDescription = null,
                    tint = tintColor,
                    modifier = Modifier.size(22.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = ArchitectTypography.Body.copy(fontWeight = FontWeight.SemiBold),
                    color = ArchitectColors.NavyPrimary,
                )
                Text(
                    text = item.subtitle,
                    style = ArchitectTypography.Caption,
                    color = ArchitectColors.MediumGray,
                )
            }
            Text(
                text = item.amount,
                style = ArchitectTypography.Body.copy(fontWeight = FontWeight.SemiBold),
                color = amtColor,
            )
        }
    }
}

private fun resolveActivityIcon(name: String): ImageVector = when (name.lowercase()) {
    "shopping_bag", "shopping", "electronics" -> Icons.Outlined.ShoppingBag
    "payments", "salary", "income", "money" -> Icons.Outlined.Payments
    "restaurant", "dining", "food" -> Icons.Outlined.Restaurant
    "car", "directions_car", "transport" -> Icons.Outlined.DirectionsCar
    "briefcase", "portfolio", "business" -> Icons.Outlined.BusinessCenter
    else -> Icons.Outlined.Receipt
}
