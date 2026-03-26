package com.architect.banking.feature.login.data.dto

import kotlinx.serialization.Serializable

/**
 * Request body for `POST /auth/login`.
 *
 * @property email The user's registered email address.
 * @property password The user's plain-text password (TLS-encrypted in transit).
 * @property deviceId A stable, unique identifier for the calling device.
 */
@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String,
    val deviceId: String,
)

/**
 * Request body for `POST /auth/refresh`.
 *
 * @property refreshToken The long-lived refresh token issued at login.
 */
@Serializable
data class RefreshRequestDto(
    val refreshToken: String,
)
