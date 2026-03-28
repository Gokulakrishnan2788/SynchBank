package com.architect.banking.feature.payments

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.architect.banking.engine.sdui.renderer.SDUIRenderer

/**
 * Add Beneficiary screen entry point.
 *
 * All rendering is delegated to [SDUIRenderer]. Effects trigger toasts and
 * pop-back navigation without coupling the UI to the ViewModel.
 *
 * @param navController Used for popBackStack after saving a beneficiary.
 * @param viewModel Injected via [hiltViewModel].
 */
@Composable
fun AddBeneficiaryScreen(
    navController: NavController,
    viewModel: AddBeneficiaryViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AddBeneficiaryEffect.Navigate -> {
                    when (effect.action.destination) {
                        "notifications" -> Toast.makeText(
                            context,
                            "Notifications coming soon",
                            Toast.LENGTH_SHORT,
                        ).show()
                        else -> Toast.makeText(context, "Not Implemented Yet", Toast.LENGTH_SHORT).show()
                    }
                }
                is AddBeneficiaryEffect.ShowToast -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
                is AddBeneficiaryEffect.PopBack -> {
                    navController.popBackStack()
                }
            }
        }
    }

    SDUIRenderer(
        screenModel = state.screenModel,
        onAction = { actionId -> viewModel.handleIntent(AddBeneficiaryIntent.HandleAction(actionId)) },
        modifier = Modifier,
        isLoading = state.isLoading,
        error = state.error,
    )
}
