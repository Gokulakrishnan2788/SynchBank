package com.architect.banking.engine.sdui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.architect.banking.core.ui.theme.ArchitectColors
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement

@Serializable
data class QuickAmount(val label: String = "", val value: Double = 0.0)

@Serializable
data class AmountInputCardProps(
    val currencySymbol: String = "$",
    val hint: String = "0.00",
    val quickAmounts: List<QuickAmount> = emptyList(),
)

@Composable
fun AmountInputCardComponent(props: JsonObject, onAction: (String) -> Unit) {
    val decoded = runCatching {
        Json { ignoreUnknownKeys = true }.decodeFromJsonElement<AmountInputCardProps>(props)
    }.getOrDefault(AmountInputCardProps())

    var amountText by rememberSaveable { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = ArchitectColors.WarmSurface,
    ) {
        Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = decoded.currencySymbol,
                    style = TextStyle(
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Light,
                        color = ArchitectColors.MediumGray,
                    ),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (amountText.isEmpty()) {
                        Text(
                            text = decoded.hint,
                            style = TextStyle(
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Light,
                                color = ArchitectColors.MediumGray.copy(alpha = 0.4f),
                            ),
                        )
                    }
                    BasicTextField(
                        value = amountText,
                        onValueChange = { v ->
                            val filtered = v.filter { it.isDigit() || it == '.' }
                            amountText = filtered
                            onAction("AMOUNT_CHANGED:$filtered")
                        },
                        textStyle = TextStyle(
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Light,
                            color = ArchitectColors.NavyPrimary,
                        ),
                        cursorBrush = SolidColor(ArchitectColors.NavyPrimary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                    )
                }
            }

            if (decoded.quickAmounts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    decoded.quickAmounts.forEach { quick ->
                        Surface(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(20.dp),
                            color = ArchitectColors.White,
                            onClick = {
                                val current = amountText.toDoubleOrNull() ?: 0.0
                                val newVal = current + quick.value
                                val formatted = if (newVal == newVal.toLong().toDouble()) {
                                    newVal.toLong().toString()
                                } else {
                                    "%.2f".format(newVal)
                                }
                                amountText = formatted
                                onAction("AMOUNT_CHANGED:$formatted")
                            },
                        ) {
                            Text(
                                text = quick.label,
                                style = MaterialTheme.typography.labelMedium,
                                color = ArchitectColors.NavyPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}
