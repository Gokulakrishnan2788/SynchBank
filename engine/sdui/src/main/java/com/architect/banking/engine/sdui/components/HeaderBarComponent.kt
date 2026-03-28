package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    val applyStatusBarPadding: Boolean = false,
    val showSearch: Boolean = false,
    val showNotification: Boolean = false,
    val searchAction: String = "HEADER_SEARCH",
    val notificationAction: String = "HEADER_NOTIFICATION",
)

private fun String.isUrl() = startsWith("http://") || startsWith("https://")

/**
 * Renders a HEADER_BAR SDUI component.
 *
 * Layout: [avatar] [title]  ···  [search icon] [notification icon]
 *
 * The left icon renders as a circular avatar — either loaded via Coil (URL)
 * or shown as the [Icons.Outlined.AccountCircle] placeholder.
 */
@Composable
fun HeaderBarComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<HeaderBarComponentProps>(props)
    }.getOrDefault(HeaderBarComponentProps())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (decoded.applyStatusBarPadding) Modifier.statusBarsPadding() else Modifier)
            .background(ArchitectColors.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // ── Left: avatar + title ──────────────────────────────────────────────
        Row(verticalAlignment = Alignment.CenterVertically) {
            val icon = decoded.icon
            if (!icon.isNullOrBlank() && icon.isUrl()) {
                AsyncImage(
                    model = icon,
                    contentDescription = "User avatar",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape),
                )
            } else {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = "User avatar",
                    tint = ArchitectColors.NavyPrimary,
                    modifier = Modifier.size(36.dp),
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = decoded.title,
                style = ArchitectTypography.Heading3.copy(fontWeight = FontWeight.Bold),
                color = ArchitectColors.NavyPrimary,
            )
        }

        // ── Right: search + notification ──────────────────────────────────────
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            if (decoded.showSearch) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { onAction(decoded.searchAction) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = "Search",
                        tint = ArchitectColors.NavyPrimary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
            if (decoded.showNotification) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .clickable { onAction(decoded.notificationAction) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = ArchitectColors.NavyPrimary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            }
        }
    }
}
