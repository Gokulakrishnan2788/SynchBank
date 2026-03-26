package com.architect.banking.feature.login.data

import com.architect.banking.core.data.db.dao.SessionDao
import com.architect.banking.core.domain.Result
import com.architect.banking.core.domain.map
import com.architect.banking.core.domain.model.AuthSession
import com.architect.banking.core.domain.repository.LoginRepository
import com.architect.banking.core.network.model.safeApiCall
import com.architect.banking.feature.login.data.api.LoginApiService
import com.architect.banking.feature.login.data.dto.LoginRequestDto
import com.architect.banking.feature.login.data.dto.RefreshRequestDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Concrete implementation of [LoginRepository].
 *
 * Coordinates between the remote [LoginApiService] and the local [SessionDao].
 * All network calls are wrapped via [safeApiCall] to produce a [Result].
 */
@Singleton
class LoginRepositoryImpl @Inject constructor(
    private val apiService: LoginApiService,
    private val sessionDao: SessionDao,
) : LoginRepository {

    override suspend fun login(email: String, password: String): Result<AuthSession> =
        safeApiCall {
            apiService.login(LoginRequestDto(email = email, password = password, deviceId = MOCK_DEVICE_ID))
        }.map { dto ->
            val session = dto.toSession()
            sessionDao.insert(session.toEntity())
            session
        }

    override suspend fun logout(): Result<Unit> {
        // Best-effort API call — local session is always cleared regardless of outcome.
        runCatching { apiService.logout() }
        sessionDao.deleteAll()
        return Result.Success(Unit)
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthSession> {
        val currentUserId = sessionDao.getActiveSession()?.userId
            ?: return Result.Error("NO_SESSION", "No active session to refresh")

        return safeApiCall {
            apiService.refresh(RefreshRequestDto(refreshToken = refreshToken))
        }.map { dto ->
            val session = AuthSession(
                userId = currentUserId,
                token = dto.token,
                refreshToken = dto.refreshToken,
                expiresAt = dto.expiresAt,
            )
            sessionDao.insert(session.toEntity())
            session
        }
    }

    override fun observeSession(): Flow<AuthSession?> =
        sessionDao.observeActiveSession().map { entity -> entity?.toSession() }

    private companion object {
        /** Placeholder device ID — replace with real DeviceIdProvider in production. */
        const val MOCK_DEVICE_ID = "device_mock_001"
    }
}
