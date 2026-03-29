package com.architect.banking.feature.accounts

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.architect.banking.engine.sdui.renderer.SDUIRenderer

@Composable
fun AccountsScreen(
    viewModel: AccountsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AccountsEffect.Navigate -> { /* no-op for now */ }
                is AccountsEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    SDUIRenderer(
        screenModel = state.screenModel,
        onAction = { actionId -> viewModel.handleIntent(AccountsIntent.HandleAction(actionId)) },
        modifier = Modifier,
        isLoading = state.isLoading,
        error = state.error,
    )
}
