package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.architect.banking.core.ui.theme.ArchitectColors
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class BeneficiaryItem(
    val id: String = "",
    val name: String = "",
    val subtitle: String = "",
    val isSelected: Boolean = false,
)

@Serializable
data class BeneficiaryGridProps(
    val beneficiaries: List<BeneficiaryItem> = emptyList(),
    val showAddNew: Boolean = true,
)

@Composable
fun BeneficiaryGridComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<BeneficiaryGridProps>(props)
    }.getOrDefault(BeneficiaryGridProps())

    var selectedId by remember {
        mutableStateOf(decoded.beneficiaries.find { it.isSelected }?.id ?: "")
    }

    val allItems: List<BeneficiaryItem?> =
        decoded.beneficiaries + if (decoded.showAddNew) listOf(null) else emptyList()
    val rows = allItems.chunked(2)

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rowItems.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        if (item == null) {
                            AddNewCard(onClick = { onAction("ADD_BENEFICIARY") })
                        } else {
                            val isSelected = item.id == selectedId
                            BeneficiaryCard(
                                item = item,
                                isSelected = isSelected,
                                onClick = {
                                    selectedId = item.id
                                    onAction("BENEFICIARY_SELECTED:${item.id}")
                                },
                            )
                        }
                    }
                }
                if (rowItems.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun BeneficiaryCard(item: BeneficiaryItem, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) ArchitectColors.NavyPrimary else Color(0xFFF2F2F4)
    val textColor = if (isSelected) ArchitectColors.White else ArchitectColors.NavyPrimary
    val subColor = if (isSelected) ArchitectColors.LightGray else ArchitectColors.MediumGray

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = bgColor,
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) Color(0xFF1A3A6B) else Color(0xFFDDDDDD),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = if (isSelected) ArchitectColors.White else ArchitectColors.MediumGray,
                        modifier = Modifier.size(32.dp),
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = textColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = subColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                )
            }
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(ArchitectColors.TealAccent),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = ArchitectColors.White,
                        modifier = Modifier.size(14.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun AddNewCard(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = Color.Transparent,
        border = BorderStroke(1.5.dp, ArchitectColors.LightGray),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(ArchitectColors.LightGray),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = ArchitectColors.MediumGray,
                    modifier = Modifier.size(22.dp),
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "NEW",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                ),
                color = ArchitectColors.MediumGray,
            )
            Text(
                text = "RECIPIENT",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                ),
                color = ArchitectColors.MediumGray,
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
