package com.architect.banking.feature.dashboard

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.architect.banking.engine.sdui.renderer.SDUIRenderer

/**
 * Dashboard screen entry point.
 *
 * All rendering delegated to [SDUIRenderer] — no hardcoded UI.
 *
 * @param navController Used by [NavigationEngine] to execute navigation effects.
 * @param viewModel Injected via [hiltViewModel].
 */
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var dialogMessage by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DashboardEffect.Navigate ->
                    Toast.makeText(context, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
                is DashboardEffect.ShowDialog ->
                    dialogMessage = effect.message
            }
        }
    }

    dialogMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { dialogMessage = null },
            text = { Text(text = message) },
            confirmButton = {
                TextButton(onClick = { dialogMessage = null }) { Text(text = "OK") }
            },
        )
    }

    SDUIRenderer(
        screenModel = state.screenModel,
        onAction = { actionId -> viewModel.handleIntent(DashboardIntent.HandleAction(actionId)) },
        modifier = Modifier,
        isLoading = state.isLoading,
        error = state.error,
    )
}
