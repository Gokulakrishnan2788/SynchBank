package com.architect.banking.feature.login

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
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
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var errorDialogMessage by rememberSaveable { mutableStateOf<String?>(null) }

    // ── Security checks ────────────────────────────────────────────────────────
    var usbDebuggingEnabled by remember { mutableStateOf(false) }
    var detectedScreenRecorders by remember { mutableStateOf<List<String>>(emptyList()) }

    fun runSecurityChecks() {
        usbDebuggingEnabled = SecurityChecker.isUsbDebuggingEnabled(context)
        detectedScreenRecorders = SecurityChecker.getInstalledScreenRecorders(context)
    }

    LaunchedEffect(Unit) { runSecurityChecks() }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) runSecurityChecks()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // USB debugging — persistent blocking dialog (cannot be dismissed without action)
    if (usbDebuggingEnabled) {
        AlertDialog(
            onDismissRequest = { /* intentionally blocked — user must disable ADB */ },
            title = { Text("Security Warning") },
            text = {
                Text(
                    "USB Debugging (ADB) is currently enabled. For your account security, " +
                        "SynchBank requires you to disable USB Debugging before signing in.\n\n" +
                        "Go to Settings → Developer Options → Disable USB Debugging.",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        try {
                            context.startActivity(
                                Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                            )
                        } catch (_: ActivityNotFoundException) {
                            context.startActivity(
                                Intent(Settings.ACTION_SETTINGS)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                            )
                        }
                    },
                ) { Text("Open Developer Options") }
            },
            dismissButton = null,
        )
    }

    // Screen recorder — persistent blocking dialog
    if (detectedScreenRecorders.isNotEmpty() && !usbDebuggingEnabled) {
        val appList = detectedScreenRecorders.joinToString("\n• ", prefix = "• ")
        AlertDialog(
            onDismissRequest = { /* intentionally blocked — user must remove recorder */ },
            title = { Text("Screen Recording Detected") },
            text = {
                Text(
                    "The following screen recording app(s) are installed on your device:\n\n$appList\n\n" +
                        "To protect your banking information, please uninstall these apps before " +
                        "continuing. Tap below to open the app settings and uninstall.",
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        context.startActivity(
                            Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                        )
                    },
                ) { Text("Open App Settings") }
            },
            dismissButton = null,
        )
    }

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
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        scope.launch {
                            snackbarHostState.showSnackbar("No browser found to open the link")
                        }
                    }
                }
                is LoginEffect.ShowBiometricPrompt -> {
                    val activity = context as? FragmentActivity
                    if (activity == null) {
                        scope.launch { snackbarHostState.showSnackbar("Biometric not supported") }
                        return@collect
                    }

                    val biometricManager = BiometricManager.from(context)
                    val fingerprintAvailable = biometricManager
                        .canAuthenticate(Authenticators.BIOMETRIC_STRONG) == BiometricManager.BIOMETRIC_SUCCESS
                    val anyBiometricAvailable = biometricManager
                        .canAuthenticate(Authenticators.BIOMETRIC_WEAK) == BiometricManager.BIOMETRIC_SUCCESS
                    // Face is considered enrolled only when WEAK biometrics are available
                    // but fingerprint (STRONG) is NOT — because STRONG satisfies WEAK too,
                    // so WEAK-only success means a face-class biometric is what's enrolled.
                    val faceAvailable = anyBiometricAvailable && !fingerprintAvailable

                    when (effect.type) {
                        BiometricType.FINGERPRINT -> {
                            if (!fingerprintAvailable) {
                                val reason = when (biometricManager.canAuthenticate(Authenticators.BIOMETRIC_STRONG)) {
                                    BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "No fingerprint hardware found on this device"
                                    BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "Fingerprint hardware is unavailable"
                                    BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "No fingerprint enrolled. Please set up in Settings → Security."
                                    else -> "Fingerprint authentication not available"
                                }
                                scope.launch { snackbarHostState.showSnackbar(reason) }
                                return@collect
                            }
                        }
                        BiometricType.FACE_ID -> {
                            if (!faceAvailable) {
                                val reason = if (!anyBiometricAvailable) {
                                    "No biometrics enrolled on this device"
                                } else {
                                    "Face ID is not set up on this device. Please enroll face recognition in Settings → Security."
                                }
                                scope.launch { snackbarHostState.showSnackbar(reason) }
                                return@collect
                            }
                        }
                    }

                    val allowedAuthenticators = when (effect.type) {
                        BiometricType.FINGERPRINT -> Authenticators.BIOMETRIC_STRONG
                        BiometricType.FACE_ID -> Authenticators.BIOMETRIC_WEAK
                    }

                    val promptTitle = when (effect.type) {
                        BiometricType.FINGERPRINT -> "Fingerprint Login"
                        BiometricType.FACE_ID -> "Face ID Login"
                    }

                    val promptInfo = BiometricPrompt.PromptInfo.Builder()
                        .setTitle(promptTitle)
                        .setSubtitle("Authenticate to access your account")
                        .setNegativeButtonText("Cancel")
                        .setAllowedAuthenticators(allowedAuthenticators)
                        .build()

                    val executor = ContextCompat.getMainExecutor(context)
                    BiometricPrompt(
                        activity,
                        executor,
                        object : BiometricPrompt.AuthenticationCallback() {
                            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                scope.launch {
                                    NavigationEngine.navigate(
                                        navController,
                                        NavigationAction(
                                            type = NavigationType.REPLACE,
                                            destination = Routes.MAIN,
                                        ),
                                    )
                                }
                            }
                            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                                if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON &&
                                    errorCode != BiometricPrompt.ERROR_USER_CANCELED
                                ) {
                                    scope.launch { snackbarHostState.showSnackbar(errString.toString()) }
                                }
                            }
                            override fun onAuthenticationFailed() {
                                scope.launch { snackbarHostState.showSnackbar("Authentication failed. Try again.") }
                            }
                        },
                    ).authenticate(promptInfo)
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
    Box(modifier = modifier.fillMaxSize().background(ArchitectColors.LoginBackground)) {
        SDUIRenderer(
            screenModel = state.screenModel,
            onAction = { actionId -> onIntent(LoginIntent.HandleAction(actionId)) },
            modifier = Modifier.fillMaxSize(),
            error = state.error,
        )
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding(),
        )
    }
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
