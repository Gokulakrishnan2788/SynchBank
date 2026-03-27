package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTypography
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

/** Props for the HEADER_BAR SDUI component. */
@Serializable
data class HeaderBarComponentProps(
    val title: String = "",
    val iconBackground: String = "NavyPrimary",
    val icon: String? = null,
)

private fun resolveLocalIcon(name: String): ImageVector = when (name.lowercase()) {
    "home" -> Icons.Default.Home
    "bank", "account_balance" -> Icons.Default.AccountBalance
    "profile", "account_circle" -> Icons.Default.AccountCircle
    else -> Icons.Default.Star
}

private fun String.isUrl() = startsWith("http://") || startsWith("https://")

private fun resolveIconBackground(token: String) = when (token) {
    "NavySecondary" -> ArchitectColors.NavySecondary
    "NavyTertiary" -> ArchitectColors.NavyTertiary
    "GoldAccent" -> ArchitectColors.GoldAccent
    else -> ArchitectColors.NavyPrimary
}

/**
 * Renders a HEADER_BAR SDUI component as a full-width white row with a branded icon and title.
 *
 * The [HeaderBarComponentProps.icon] field accepts either:
 * - A URL string → loaded via Coil [AsyncImage]
 * - An icon name (e.g. "bank", "home") → resolved to a local [ImageVector]
 *
 * @param props Raw JSON props decoded into [HeaderBarComponentProps].
 * @param onAction Unused; present for registry interface consistency.
 */
@Composable
fun HeaderBarComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json.decodeFromJsonElement<HeaderBarComponentProps>(props)
    }.getOrDefault(HeaderBarComponentProps())

    val bgColor = resolveIconBackground(decoded.iconBackground)

    Row(
        modifier = Modifier
            .fillMaxWidth().statusBarsPadding()
            .background(ArchitectColors.White)
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(bgColor, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
        ) {
            val icon = decoded.icon
            if (!icon.isNullOrBlank() && icon.isUrl()) {
                AsyncImage(
                    model = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            } else {
                val imageVector = if (!icon.isNullOrBlank()) resolveLocalIcon(icon)
                else Icons.Default.AccountBalance
                Icon(
                    imageVector = imageVector,
                    contentDescription = null,
                    tint = ArchitectColors.White,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
        Text(
            text = decoded.title,
            style = ArchitectTypography.Heading3.copy(fontWeight = FontWeight.Bold),
            color = ArchitectColors.NavyPrimary,
        )
    }
}
