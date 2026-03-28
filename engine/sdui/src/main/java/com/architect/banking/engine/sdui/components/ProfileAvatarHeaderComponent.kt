package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTypography
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class ProfileAvatarHeaderProps(
    val name: String = "Alexander Sterling",
    val memberSince: String = "MEMBER SINCE JANUARY 2022",
    val avatarSize: Int = 100,
    val imagePath: String? = null,
)

@Composable
fun ProfileAvatarHeaderComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<ProfileAvatarHeaderProps>(props)
    }.getOrDefault(ProfileAvatarHeaderProps())

    Column {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(
                modifier = Modifier
                    .size(decoded.avatarSize.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(ArchitectColors.NavySecondary),
                contentAlignment = Alignment.Center,
            ) {
                if (!decoded.imagePath.isNullOrBlank()) {
                    AsyncImage(
                        model = decoded.imagePath,
                        contentDescription = "Profile photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = ArchitectColors.White,
                        modifier = Modifier.size((decoded.avatarSize * 0.6).dp),
                    )
                }
            }
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .offset(x = 4.dp, y = 4.dp)
                    .clip(CircleShape)
                    .background(ArchitectColors.TealAccent)
                    .clickable { onAction("EDIT_PROFILE") }
                    .padding(6.dp),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit profile",
                    tint = ArchitectColors.White,
                    modifier = Modifier.size(14.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = decoded.name,
            style = ArchitectTypography.Heading1.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = ArchitectColors.NavyPrimary,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = decoded.memberSince,
            style = ArchitectTypography.Label,
            color = ArchitectColors.MediumGray,
        )
    }
}
