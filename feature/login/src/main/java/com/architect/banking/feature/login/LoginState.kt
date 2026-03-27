package com.architect.banking.feature.login

import com.architect.banking.core.domain.UiEffect
import com.architect.banking.core.domain.UiIntent
import com.architect.banking.core.domain.UiState
import com.architect.banking.engine.sdui.model.ScreenModel

/** Available biometric authentication methods surfaced by the SDUI BIOMETRIC_ROW component. */
enum class BiometricType { FINGERPRINT, FACE_ID }

/**
 * Immutable UI state for the Login screen.
 *
 * @property isLoading True while a network or asset-load operation is in flight.
 * @property error Inline error message passed to [SDUIRenderer]; null when clean.
 * @property screenModel Parsed SDUI screen definition. Null while loading.
 * @property usernameInput Current value of the username/email field.
 * @property passwordInput Current value of the password field.
 * @property isBiometricAvailable True when the device supports biometric auth.
 */
data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val screenModel: ScreenModel? = null,
    val usernameInput: String = "",
    val passwordInput: String = "",
    val isBiometricAvailable: Boolean = false,
) : UiState

/**
 * All user-initiated actions on the Login screen.
 */
sealed class LoginIntent : UiIntent {

    /** Fetches the login SDUI screen definition for [screenId]. */
    data class LoadScreen(val screenId: String = "login") : LoginIntent()

    /** User has changed the username/email field to [value]. */
    data class UsernameChanged(val value: String) : LoginIntent()

    /** User has changed the password field to [value]. */
    data class PasswordChanged(val value: String) : LoginIntent()

    /** User tapped the primary login button — triggers validation + API call. */
    object Submit : LoginIntent()

    /** User tapped "Forgot password". */
    object ForgotPassword : LoginIntent()

    /** User selected a biometric method from the BIOMETRIC_ROW component. */
    data class BiometricSelected(val type: BiometricType) : LoginIntent()

    /** SDUI component dispatched action with [actionId]. */
    data class HandleAction(val actionId: String) : LoginIntent()

    /** Clears the current inline error state. */
    object ClearError : LoginIntent()
}

/**
 * One-shot side effects for the Login screen.
 */
sealed class LoginEffect : UiEffect {

    /** Navigate to the dashboard, replacing the login screen in the back stack. */
    object NavigateToDashboard : LoginEffect()

    /** Navigate to the forgot-password flow. */
    object NavigateToForgotPassword : LoginEffect()

    /** Launch the external institutional inquiry deep link. */
    object NavigateToInquire : LoginEffect()

    /**
     * Launch the system biometric prompt for [type].
     *
     * @property type The biometric method the user selected.
     */
    data class ShowBiometricPrompt(val type: BiometricType) : LoginEffect()

    /**
     * Display a transient error message (e.g. network failure outside normal login flow).
     *
     * @property message Human-readable error description.
     */
    data class ShowError(val message: String) : LoginEffect()

    /**
     * Display a transient validation error as a toast/snackbar.
     * Triggered by client-side field validation failures (blank email, short password, etc.).
     * These are NOT stored in state — they are one-shot events.
     *
     * @property message Human-readable validation failure description.
     */
    data class ShowValidationError(val message: String) : LoginEffect()
}
