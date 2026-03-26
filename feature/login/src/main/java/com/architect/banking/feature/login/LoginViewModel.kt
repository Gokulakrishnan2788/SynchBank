package com.architect.banking.feature.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.architect.banking.core.domain.BaseViewModel
import com.architect.banking.core.domain.NoParams
import com.architect.banking.core.domain.Result
import com.architect.banking.core.domain.onError
import com.architect.banking.core.domain.onSuccess
import com.architect.banking.engine.sdui.parser.SDUIParser
import com.architect.banking.feature.login.domain.GetSessionUseCase
import com.architect.banking.feature.login.domain.LoginUseCase
import com.architect.banking.feature.login.domain.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MVI ViewModel for the Login screen.
 *
 * On creation:
 * 1. Checks for an existing valid session via [GetSessionUseCase] — auto-navigates to
 *    Dashboard if one is found so the user is never shown the login form unnecessarily.
 * 2. Dispatches [LoginIntent.LoadScreen] to fetch the SDUI screen definition.
 *
 * The [LogoutUseCase] is injected here so it can be wired to a future "sign out from
 * Dashboard back to Login" flow without modifying the constructor.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getSessionUseCase: GetSessionUseCase,
    private val sduiParser: SDUIParser,
    @ApplicationContext private val context: Context,
) : BaseViewModel<LoginState, LoginIntent, LoginEffect>() {

    override fun initialState() = LoginState()

    init {
        checkExistingSession()
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
            is LoginIntent.HandleAction -> handleAction(intent.actionId)
            is LoginIntent.ClearError -> setState { copy(error = null) }
        }
    }

    /**
     * Checks for an existing valid session on init.
     * Takes only the first emission to avoid re-triggering navigation on session changes.
     */
    private fun checkExistingSession() {
        viewModelScope.launch {
            val result = getSessionUseCase(NoParams).first()
            if (result is Result.Success && result.data != null) {
                setEffect(LoginEffect.NavigateToDashboard)
            }
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
                setState { copy(isLoading = false, error = error.message) }
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
        when (actionId) {
            "SUBMIT_FORM" -> submitLogin()
            "FORGOT_PASSWORD" -> setEffect(LoginEffect.NavigateToForgotPassword)
            "INQUIRE" -> setEffect(LoginEffect.NavigateToInquire)
        }
    }
}
