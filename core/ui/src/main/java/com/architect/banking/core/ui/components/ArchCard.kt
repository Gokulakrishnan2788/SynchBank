package com.architect.banking.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectElevation
import com.architect.banking.core.ui.theme.ArchitectSpacing
import com.architect.banking.core.ui.theme.ArchitectTheme

/**
 * Design-system card matching the SDUI CARD component.
 *
 * @param modifier Optional layout modifier.
 * @param elevation Shadow elevation. Defaults to [ArchitectElevation.SM].
 * @param content Composable content rendered inside the card.
 */
@Composable
fun ArchCard(
    modifier: Modifier = Modifier,
    elevation: Float = ArchitectElevation.SM.value,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(ArchitectSpacing.SM),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp),
        colors = CardDefaults.cardColors(
            containerColor = ArchitectColors.CardBackground,
        ),
    ) {
        Column(
            modifier = Modifier.padding(ArchitectSpacing.MD),
            content = content,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ArchCardPreview() {
    ArchitectTheme {
        ArchCard {
            Text(
                text = "Card Content",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}
