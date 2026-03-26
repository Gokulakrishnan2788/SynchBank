package com.architect.banking.feature.profile

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.architect.banking.engine.navigation.NavigationEngine
import com.architect.banking.engine.sdui.renderer.SDUIRenderer

/**
 * Profile screen entry point.
 *
 * All rendering delegated to [SDUIRenderer] — no hardcoded UI.
 *
 * @param navController Used by [NavigationEngine] to execute navigation effects.
 * @param viewModel Injected via [hiltViewModel].
 */
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var dialogMessage by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileEffect.Navigate ->
                    NavigationEngine.navigate(navController, effect.action)
                is ProfileEffect.ShowDialog ->
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
        onAction = { actionId -> viewModel.handleIntent(ProfileIntent.HandleAction(actionId)) },
        modifier = Modifier,
        error = state.error,
    )
}
