package com.architect.banking.feature.login.data

import com.architect.banking.core.data.db.entity.SessionEntity
import com.architect.banking.core.domain.model.AuthSession
import com.architect.banking.feature.login.data.dto.LoginResponseDto

/**
 * Maps a [LoginResponseDto] (network layer) to an [AuthSession] (domain layer).
 * Only the four session-critical fields are mapped; UI-only fields (userName, accountType)
 * are not part of the domain model.
 */
fun LoginResponseDto.toSession(): AuthSession = AuthSession(
    userId = userId,
    token = token,
    refreshToken = refreshToken,
    expiresAt = expiresAt,
)

/**
 * Maps an [AuthSession] (domain layer) to a [SessionEntity] (data layer) for Room persistence.
 */
fun AuthSession.toEntity(): SessionEntity = SessionEntity(
    userId = userId,
    token = token,
    refreshToken = refreshToken,
    expiresAt = expiresAt,
)

/**
 * Maps a [SessionEntity] (data layer) back to an [AuthSession] (domain layer).
 */
fun SessionEntity.toSession(): AuthSession = AuthSession(
    userId = userId,
    token = token,
    refreshToken = refreshToken,
    expiresAt = expiresAt,
)
