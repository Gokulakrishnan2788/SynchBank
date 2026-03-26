package com.architect.banking.core.domain.repository

import com.architect.banking.core.domain.Result
import com.architect.banking.core.domain.model.AuthSession
import kotlinx.coroutines.flow.Flow

/**
 * Contract for all login / session operations.
 *
 * The interface lives in `:core:domain` so ViewModels and UseCases can depend on the
 * abstraction. The implementation ([LoginRepositoryImpl]) lives in `:feature:login`.
 */
interface LoginRepository {

    /**
     * Authenticates the user against the remote API and persists the session locally.
     *
     * @param email The user's registered email address.
     * @param password The user's plain-text password.
     * @return [Result.Success] with the created [AuthSession], or [Result.Error] on failure.
     */
    suspend fun login(email: String, password: String): Result<AuthSession>

    /**
     * Invalidates the current session on the remote API and clears the local session store.
     *
     * @return [Result.Success] with [Unit]. Never returns [Result.Error] — local session is
     * always cleared even when the API call fails.
     */
    suspend fun logout(): Result<Unit>

    /**
     * Exchanges the current refresh token for a new access token pair and updates local storage.
     *
     * @param refreshToken The long-lived refresh token from the current [AuthSession].
     * @return [Result.Success] with the refreshed [AuthSession], or [Result.Error] on failure.
     */
    suspend fun refreshToken(refreshToken: String): Result<AuthSession>

    /**
     * Observes the active session reactively.
     * Emits `null` when the user is signed out or no session exists.
     */
    fun observeSession(): Flow<AuthSession?>
}
