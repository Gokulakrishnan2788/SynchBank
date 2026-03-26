# Login — MVI Contract

## LoginState
data class LoginState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val screenModel: ScreenModel? = null,    // SDUI JSON parsed model
    val usernameInput: String = "",
    val passwordInput: String = "",
    val isBiometricAvailable: Boolean = false
) : UiState

## LoginIntent
sealed class LoginIntent : UiIntent {
    data class LoadScreen(val screenId: String = "login") : LoginIntent()
    data class UsernameChanged(val value: String) : LoginIntent()
    data class PasswordChanged(val value: String) : LoginIntent()
    object Submit : LoginIntent()
    object ForgotPassword : LoginIntent()
    data class BiometricSelected(val type: BiometricType) : LoginIntent()
    data class HandleAction(val actionId: String) : LoginIntent()
    object ClearError : LoginIntent()
}

## LoginEffect
sealed class LoginEffect : UiEffect {
    object NavigateToDashboard : LoginEffect()
    object NavigateToForgotPassword : LoginEffect()
    object NavigateToInquire : LoginEffect()
    data class ShowBiometricPrompt(val type: BiometricType) : LoginEffect()
    data class ShowError(val message: String) : LoginEffect()
}

## Reducer Logic
LoadScreen → fetch login_screen.json → setState(screenModel)
UsernameChanged → setState(usernameInput = value)
PasswordChanged → setState(passwordInput = value)
Submit → validate → LoginUseCase → success: NavigateToDashboard / fail: error state
HandleAction("FORGOT_PASSWORD") → setEffect(NavigateToForgotPassword)
HandleAction("SUBMIT_FORM") → same as Submit
HandleAction("INQUIRE") → setEffect(NavigateToInquire)
BiometricSelected → setEffect(ShowBiometricPrompt)
