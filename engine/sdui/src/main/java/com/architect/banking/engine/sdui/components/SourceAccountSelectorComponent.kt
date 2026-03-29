package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class AccountOption(
    val id: String = "",
    val name: String = "",
    val maskedNumber: String = "",
    val balance: String = "",
)

@Serializable
data class SourceAccountSelectorProps(
    val accounts: List<AccountOption> = emptyList(),
    val defaultSelectedId: String = "",
    val balanceColor: String = "TealAccent",
)

@Composable
fun SourceAccountSelectorComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<SourceAccountSelectorProps>(props)
    }.getOrDefault(SourceAccountSelectorProps())

    val defaultAcc = decoded.accounts.find { it.id == decoded.defaultSelectedId }
        ?: decoded.accounts.firstOrNull()
        ?: AccountOption()

    var selected by remember { mutableStateOf(defaultAcc) }
    var expanded by remember { mutableStateOf(false) }

    val balColor = if (decoded.balanceColor.lowercase().contains("teal")) ArchitectColors.TealAccent
    else ArchitectColors.NavyPrimary

    Column(modifier = Modifier.fillMaxWidth()) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded },
            shape = RoundedCornerShape(12.dp),
            color = ArchitectColors.White,
            shadowElevation = 2.dp,
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${selected.name} \u2014 ${selected.maskedNumber}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = ArchitectColors.NavyPrimary,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = ArchitectColors.MediumGray,
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Available Balance",
                        style = MaterialTheme.typography.bodySmall,
                        color = ArchitectColors.MediumGray,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = selected.balance,
                        style = MaterialTheme.typography.bodyMedium,
                        color = balColor,
                    )
                }
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            decoded.accounts.forEach { account ->
                DropdownMenuItem(
                    text = {
                        Column {
                            Text(
                                text = "${account.name} \u2014 ${account.maskedNumber}",
                                color = ArchitectColors.NavyPrimary,
                            )
                            Text(
                                text = account.balance,
                                style = MaterialTheme.typography.bodySmall,
                                color = ArchitectColors.MediumGray,
                            )
                        }
                    },
                    onClick = {
                        selected = account
                        expanded = false
                        onAction("ACCOUNT_SELECTED:${account.id}")
                    },
                )
            }
        }
    }
}
