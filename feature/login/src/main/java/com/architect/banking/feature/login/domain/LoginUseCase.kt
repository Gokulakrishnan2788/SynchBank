package com.architect.banking.feature.login.domain

import com.architect.banking.core.domain.Result
import com.architect.banking.core.domain.UseCase
import com.architect.banking.core.domain.model.AuthSession
import com.architect.banking.core.domain.repository.LoginRepository
import javax.inject.Inject

/**
 * Validates login credentials and delegates to [LoginRepository.login].
 *
 * All input validation is performed here — not in the ViewModel or Repository.
 */
class LoginUseCase @Inject constructor(
    private val repository: LoginRepository,
) : UseCase<LoginUseCase.Params, AuthSession>() {

    /**
     * @property email The user's registered email address.
     * @property password The user's plain-text password.
     */
    data class Params(val email: String, val password: String)

    override suspend fun invoke(params: Params): Result<AuthSession> {
        val emailError = validateEmail(params.email)
        if (emailError != null) return Result.Error("VALIDATION_ERROR", emailError)

        val passwordError = validatePassword(params.password)
        if (passwordError != null) return Result.Error("VALIDATION_ERROR", passwordError)

        return repository.login(params.email, params.password)
    }

    private fun validateEmail(email: String): String? = when {
        email.isBlank() -> "Email must not be blank"
        !EMAIL_REGEX.matches(email) -> "Invalid email format"
        else -> null
    }

    private fun validatePassword(password: String): String? = when {
        password.isBlank() -> "Password must not be blank"
        password.length < MIN_PASSWORD_LENGTH -> "Password must be at least $MIN_PASSWORD_LENGTH characters"
        else -> null
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,}$")
        const val MIN_PASSWORD_LENGTH = 6
    }
}
