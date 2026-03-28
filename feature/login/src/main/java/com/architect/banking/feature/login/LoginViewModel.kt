package com.architect.banking.feature.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.architect.banking.core.domain.BaseViewModel
import com.architect.banking.core.domain.Result
import com.architect.banking.core.domain.onError
import com.architect.banking.core.domain.onSuccess
import com.architect.banking.engine.sdui.parser.SDUIParser
import com.architect.banking.feature.login.domain.LoginUseCase
import com.architect.banking.feature.login.domain.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MVI ViewModel for the Login screen.
 *
 * On creation dispatches [LoginIntent.LoadScreen] to fetch the SDUI screen definition.
 * Navigation to Dashboard only happens on explicit login button press + successful validation.
 *
 * The [LogoutUseCase] is injected here so it can be wired to a future "sign out from
 * Dashboard back to Login" flow without modifying the constructor.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val sduiParser: SDUIParser,
    @ApplicationContext private val context: Context,
) : BaseViewModel<LoginState, LoginIntent, LoginEffect>() {

    override fun initialState() = LoginState()

    init {
        handleIntent(LoginIntent.LoadScreen())
    }

    override suspend fun reduce(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.LoadScreen -> loadScreen(intent.screenId)
            is LoginIntent.UsernameChanged -> setState { copy(usernameInput = intent.value, error = null) }
            is LoginIntent.PasswordChanged -> setState { copy(passwordInput = intent.value, error = null) }
            is LoginIntent.Submit -> submitLogin()
            is LoginIntent.ForgotPassword -> setEffect(LoginEffect.NavigateToForgotPassword)
            is LoginIntent.BiometricSelected -> setEffect(LoginEffect.ShowBiometricPrompt(intent.type))
            is LoginIntent.BiometricLoginSuccess -> setEffect(LoginEffect.NavigateToDashboard)
            is LoginIntent.HandleAction -> handleAction(intent.actionId)
            is LoginIntent.ClearError -> setState { copy(error = null) }
        }
    }

    /**
     * Loads the SDUI screen definition from the local mock asset.
     * [SDUIParser] deserialises the JSON into a [ScreenModel] stored in state.
     */
    private suspend fun loadScreen(screenId: String) {
        setState { copy(isLoading = true, error = null) }
        try {
            val json = context.assets
                .open("mock/screens/${screenId}_screen.json")
                .bufferedReader()
                .use { it.readText() }
            when (val result = sduiParser.parse(json)) {
                is Result.Success -> setState { copy(isLoading = false, screenModel = result.data) }
                is Result.Error -> setState { copy(isLoading = false, error = result.message) }
                is Result.Loading -> Unit
            }
        } catch (e: Exception) {
            setState { copy(isLoading = false, error = "Failed to load screen: ${e.message}") }
        }
    }

    /**
     * Validates credentials via [LoginUseCase] (which owns all validation logic)
     * and navigates to the Dashboard on success.
     */
    private suspend fun submitLogin() {
        setState { copy(isLoading = true, error = null) }
        loginUseCase(
            LoginUseCase.Params(
                email = state.value.usernameInput,
                password = state.value.passwordInput,
            ),
        )
            .onSuccess {
                setState { copy(isLoading = false) }
                setEffect(LoginEffect.NavigateToDashboard)
            }
            .onError { error ->
                if (error.code == "VALIDATION_ERROR") {
                    // Validation failures are one-shot events — show as toast, do not persist in state.
                    setState { copy(isLoading = false) }
                    setEffect(LoginEffect.ShowValidationError(error.message))
                } else {
                    // API / network errors are persistent — shown inline in the UI via state.
                    setState { copy(isLoading = false, error = error.message) }
                }
            }
    }

    /**
     * Routes SDUI action IDs to the appropriate [LoginIntent].
     *
     * SDUI actions defined in `login_screen.json`:
     * - `SUBMIT_FORM` → treated identically to [LoginIntent.Submit]
     * - `FORGOT_PASSWORD` → navigate to forgot-password screen
     * - `INQUIRE` → launch institutional onboarding deep link
     */
    private suspend fun handleAction(actionId: String) {
        when {
            actionId.startsWith("FIELD_CHANGE:") -> {
                // Format: "FIELD_CHANGE:<fieldId>:<value>"
                val parts = actionId.split(":", limit = 3)
                if (parts.size == 3) {
                    val fieldId = parts[1]
                    val value = parts[2]
                    when (fieldId) {
                        "username_field" -> setState { copy(usernameInput = value, error = null) }
                        "password_field" -> setState { copy(passwordInput = value, error = null) }
                    }
                }
            }
            actionId == "SUBMIT_FORM" -> submitLogin()
            actionId == "FORGOT_PASSWORD" -> setEffect(LoginEffect.NavigateToForgotPassword)
            actionId == "INQUIRE" -> setEffect(LoginEffect.NavigateToInquire)
            actionId == "BIOMETRIC_FINGERPRINT" -> setEffect(LoginEffect.ShowBiometricPrompt(BiometricType.FINGERPRINT))
            actionId == "BIOMETRIC_FACE_ID" -> setEffect(LoginEffect.ShowBiometricPrompt(BiometricType.FACE_ID))
        }
    }
}
