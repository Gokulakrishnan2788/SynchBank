package com.architect.banking.feature.payments

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
 * Transfer (Payments) screen entry point.
 *
 * All rendering is delegated to [SDUIRenderer]. Effects are collected here
 * to handle navigation and toasts without coupling UI to ViewModel internals.
 *
 * @param navController Used to navigate to the add_beneficiary destination.
 * @param viewModel Injected via [hiltViewModel].
 */
@Composable
fun TransferScreen(
    navController: NavController,
    viewModel: TransferViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var dialogMessage by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TransferEffect.Navigate -> {
                    when (effect.action.destination) {
                        "add_beneficiary" -> navController.navigate("add_beneficiary")
                        "notifications" -> Toast.makeText(
                            context,
                            "Notifications coming soon",
                            Toast.LENGTH_SHORT,
                        ).show()
                        else -> Toast.makeText(context, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
                    }
                }
                is TransferEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is TransferEffect.ShowDialog -> {
                    dialogMessage = effect.message
                }
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
        onAction = { actionId -> viewModel.handleIntent(TransferIntent.HandleAction(actionId)) },
        modifier = Modifier,
        isLoading = state.isLoading,
        error = state.error,
    )
}
