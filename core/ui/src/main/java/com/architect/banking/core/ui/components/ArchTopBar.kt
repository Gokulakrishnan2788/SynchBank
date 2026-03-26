package com.architect.banking.core.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTheme

/**
 * Design-system top app bar with optional back navigation and action slots.
 *
 * @param title Screen title text.
 * @param modifier Optional layout modifier.
 * @param onBack Optional back navigation callback. When non-null, a back arrow is shown.
 * @param actions Optional composable for trailing action icons.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchTopBar(
    title: String,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    actions: @Composable () -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back",
                    )
                }
            }
        },
        actions = { actions() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ArchitectColors.NavyPrimary,
            titleContentColor = ArchitectColors.White,
            navigationIconContentColor = ArchitectColors.White,
            actionIconContentColor = ArchitectColors.White,
        ),
    )
}

@Preview
@Composable
private fun ArchTopBarPreview() {
    ArchitectTheme {
        ArchTopBar(title = "Dashboard")
    }
}

@Preview
@Composable
private fun ArchTopBarWithBackPreview() {
    ArchitectTheme {
        ArchTopBar(title = "Account Details", onBack = {})
    }
}
