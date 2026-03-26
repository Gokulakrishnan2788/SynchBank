package com.architect.banking.feature.login.domain

import com.architect.banking.core.domain.NoParams
import com.architect.banking.core.domain.Result
import com.architect.banking.core.domain.UseCase
import com.architect.banking.core.domain.repository.LoginRepository
import javax.inject.Inject

/**
 * Logs the user out by invalidating the remote session and clearing local storage.
 *
 * Always returns [Result.Success] — logout is considered complete even when the API
 * call fails, because the local session is always cleared by [LoginRepository.logout].
 */
class LogoutUseCase @Inject constructor(
    private val repository: LoginRepository,
) : UseCase<NoParams, Unit>() {

    override suspend fun invoke(params: NoParams): Result<Unit> =
        repository.logout()
}
