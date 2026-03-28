package com.architect.banking.feature.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTheme
import com.architect.banking.engine.navigation.NavigationAction
import com.architect.banking.engine.navigation.NavigationEngine
import com.architect.banking.engine.navigation.NavigationType
import com.architect.banking.engine.navigation.Routes
import com.architect.banking.engine.sdui.renderer.SDUIRenderer
import kotlinx.coroutines.launch

/**
 * Login screen entry point.
 *
 * Collects [LoginState] from [LoginViewModel] and handles [LoginEffect] one-shot events:
 * - [LoginEffect.NavigateToDashboard] → replace login with dashboard (no back entry).
 * - [LoginEffect.NavigateToForgotPassword] → push forgot-password screen.
 * - [LoginEffect.NavigateToInquire] → launch institutional onboarding deep link.
 * - [LoginEffect.ShowBiometricPrompt] → launch system biometric prompt.
 * - [LoginEffect.ShowError] → display transient snackbar message.
 *
 * All component rendering is delegated to [SDUIRenderer] — no hardcoded layout.
 *
 * @param navController Used by [NavigationEngine] to execute navigation effects.
 * @param viewModel Injected via [hiltViewModel].
 */
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var errorDialogMessage by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                LoginEffect.NavigateToDashboard -> NavigationEngine.navigate(
                    navController,
                    NavigationAction(
                        type = NavigationType.REPLACE,
                        destination = Routes.MAIN,
                    ),
                )
                LoginEffect.NavigateToForgotPassword -> NavigationEngine.navigate(
                    navController,
                    NavigationAction(destination = Routes.FORGOT_PASSWORD),
                )
                LoginEffect.NavigateToInquire -> {
                    // Deep link to external institutional onboarding flow.
                    NavigationEngine.navigate(
                        navController,
                        NavigationAction(
                            type = NavigationType.DEEP_LINK,
                            deepLink = "architect://onboarding/inquire",
                        ),
                    )
                }
                is LoginEffect.ShowBiometricPrompt -> {
                    // TODO: Wire androidx.biometric BiometricPrompt with LocalContext activity.
                    // For now surfaces the type as a snackbar hint.
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Biometric: ${effect.type.name}"
                        )
                    }
                }
                is LoginEffect.ShowError -> {
                    scope.launch { snackbarHostState.showSnackbar(effect.message) }
                }
                is LoginEffect.ShowValidationError -> {
                    scope.launch { snackbarHostState.showSnackbar(effect.message) }
                }
            }
        }
    }

    errorDialogMessage?.let { message ->
        ErrorDialog(
            message = message,
            onDismiss = {
                errorDialogMessage = null
                viewModel.handleIntent(LoginIntent.ClearError)
            },
        )
    }

    LoginScreenContent(
        state = state,
        onIntent = viewModel::handleIntent,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun LoginScreenContent(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    modifier: Modifier = Modifier,
) {
    SDUIRenderer(
        screenModel = state.screenModel,
        onAction = { actionId -> onIntent(LoginIntent.HandleAction(actionId)) },
        modifier = modifier.fillMaxSize().background(ArchitectColors.LoginBackground),
        error = state.error,
    )

    SnackbarHost(hostState = snackbarHostState)
}

@Composable
private fun ErrorDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Error") },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "OK")
            }
        },
    )
}

// ── Previews ──────────────────────────────────────────────────────────────────

@Preview(name = "Login — Loading", showBackground = true)
@Composable
private fun PreviewLoginLoading() {
    ArchitectTheme {
        LoginScreenContent(
            state = LoginState(isLoading = true),
            onIntent = {},
        )
    }
}

@Preview(name = "Login — Error", showBackground = true)
@Composable
private fun PreviewLoginError() {
    ArchitectTheme {
        LoginScreenContent(
            state = LoginState(error = "Invalid credentials. Please try again."),
            onIntent = {},
        )
    }
}
