package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.BusinessCenter
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.sp
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTypography
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class AccountCardProps(
    val theme: String = "WHITE",
    val iconName: String = "bank",
    val maskedNumber: String = "",
    val accountName: String = "",
    val accountSubtitle: String = "",
    val badge: String? = null,
    val badgeColor: String = "Success",
    val primaryLabel: String = "",
    val primaryAmount: String = "",
    val secondaryLabel: String? = null,
    val secondaryAmount: String? = null,
    val secondaryAmountColor: String = "NavyPrimary",
    val buttonLabel: String = "Manage \u2192",
    val equityPercent: String? = null,
    val equityAmount: String? = null,
    val cashPercent: String? = null,
    val cashAmount: String? = null,
)

@Composable
fun AccountCardComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<AccountCardProps>(props)
    }.getOrDefault(AccountCardProps())

    val isDark = decoded.theme.uppercase() == "DARK"
    val cardBg = if (isDark) ArchitectColors.NavyPrimary else ArchitectColors.White
    val titleColor = if (isDark) ArchitectColors.White else ArchitectColors.NavyPrimary
    val subtitleColor = if (isDark) ArchitectColors.LightGray else ArchitectColors.MediumGray
    val labelColor = if (isDark) ArchitectColors.LightGray else ArchitectColors.MediumGray
    val amountColor = if (isDark) ArchitectColors.White else ArchitectColors.NavyPrimary
    val maskedColor = if (isDark) ArchitectColors.LightGray else ArchitectColors.MediumGray

    val secondaryColor = when (decoded.secondaryAmountColor.lowercase()) {
        "tealaccent", "teal" -> ArchitectColors.TealAccent
        "success" -> ArchitectColors.Success
        "error" -> ArchitectColors.Error
        else -> if (isDark) ArchitectColors.White else ArchitectColors.NavyPrimary
    }

    val badgeBg = when (decoded.badgeColor.lowercase()) {
        "success" -> ArchitectColors.Success
        "tealaccent", "teal" -> ArchitectColors.TealAccent
        else -> ArchitectColors.Success
    }

    val iconVec = resolveAccountIcon(decoded.iconName)

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = cardBg,
        shadowElevation = if (isDark) 0.dp else 4.dp,
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Top row: icon left + masked number right
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isDark) ArchitectColors.TealAccent.copy(alpha = 0.25f)
                            else ArchitectColors.NavyPrimary
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = iconVec,
                        contentDescription = null,
                        tint = if (isDark) ArchitectColors.TealAccent else ArchitectColors.White,
                        modifier = Modifier.size(24.dp),
                    )
                }
                Text(
                    text = decoded.maskedNumber,
                    style = ArchitectTypography.Caption,
                    color = maskedColor,
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Account name + subtitle
            Text(
                text = decoded.accountName,
                style = ArchitectTypography.Heading2.copy(fontWeight = FontWeight.Bold),
                color = titleColor,
            )
            Text(
                text = decoded.accountSubtitle,
                style = ArchitectTypography.Caption,
                color = subtitleColor,
                modifier = Modifier.padding(top = 2.dp),
            )

            // Optional badge (e.g. "4.85% APY")
            if (!decoded.badge.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = decoded.badge,
                    style = ArchitectTypography.Label.copy(fontWeight = FontWeight.SemiBold),
                    color = Color.White,
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(badgeBg)
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isDark) {
                HorizontalDivider(color = ArchitectColors.LightGray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Primary balance
            Text(
                text = decoded.primaryLabel,
                style = ArchitectTypography.Label,
                color = labelColor,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = decoded.primaryAmount,
                style = if (isDark) ArchitectTypography.Display.copy(fontSize = 34.sp) else ArchitectTypography.Heading2.copy(fontWeight = FontWeight.Bold),
                color = amountColor,
            )

            // Secondary balance or equity/cash row
            if (!decoded.equityPercent.isNullOrBlank()) {
                // Wealth management equity/cash stats
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("EQUITY", style = ArchitectTypography.Label, color = labelColor)
                        Text(
                            text = decoded.equityPercent!!,
                            style = ArchitectTypography.Heading3.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                            color = amountColor,
                        )
                        Text(decoded.equityAmount ?: "", style = ArchitectTypography.Caption, color = subtitleColor)
                    }
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(48.dp)
                            .background(ArchitectColors.LightGray.copy(alpha = 0.4f))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("CASH", style = ArchitectTypography.Label, color = labelColor)
                        Text(
                            text = decoded.cashPercent!!,
                            style = ArchitectTypography.Heading3.copy(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                            color = amountColor,
                        )
                        Text(decoded.cashAmount ?: "", style = ArchitectTypography.Caption, color = subtitleColor)
                    }
                }
            } else if (!decoded.secondaryLabel.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = decoded.secondaryLabel,
                    style = ArchitectTypography.Label,
                    color = labelColor,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = decoded.secondaryAmount ?: "",
                    style = ArchitectTypography.Body.copy(fontWeight = FontWeight.SemiBold),
                    color = secondaryColor,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Manage button
            if (isDark) {
                OutlinedButton(
                    onClick = { onAction("MANAGE_ACCOUNT:${decoded.accountName}") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ArchitectColors.White),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ArchitectColors.White),
                ) {
                    Text(
                        text = decoded.buttonLabel,
                        style = ArchitectTypography.ButtonText.copy(fontSize = 14.sp),
                        color = ArchitectColors.White,
                    )
                }
            } else {
                OutlinedButton(
                    onClick = { onAction("MANAGE_ACCOUNT:${decoded.accountName}") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, ArchitectColors.TealAccent),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = ArchitectColors.TealAccent),
                ) {
                    Text(
                        text = decoded.buttonLabel,
                        style = ArchitectTypography.ButtonText.copy(fontSize = 14.sp),
                        color = ArchitectColors.TealAccent,
                    )
                }
            }
        }
    }
}

private fun resolveAccountIcon(name: String): ImageVector = when (name.lowercase()) {
    "bank", "account_balance" -> Icons.Outlined.AccountBalance
    "savings" -> Icons.Outlined.Savings
    "briefcase", "portfolio", "business" -> Icons.Outlined.BusinessCenter
    else -> Icons.Outlined.AccountBalance
}
