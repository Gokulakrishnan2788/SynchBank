package com.architect.banking.feature.login.domain

import com.architect.banking.core.domain.FlowUseCase
import com.architect.banking.core.domain.NoParams
import com.architect.banking.core.domain.Result
import com.architect.banking.core.domain.model.AuthSession
import com.architect.banking.core.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Observes the active [AuthSession] reactively.
 *
 * Primarily used on app launch to determine whether to show the login screen or
 * navigate directly to the dashboard. Emits [Result.Success] wrapping `null` when
 * no session exists (user is logged out).
 */
class GetSessionUseCase @Inject constructor(
    private val repository: LoginRepository,
) : FlowUseCase<NoParams, AuthSession?>() {

    override fun invoke(params: NoParams): Flow<Result<AuthSession?>> =
        repository.observeSession().map { session -> Result.Success(session) }
}
