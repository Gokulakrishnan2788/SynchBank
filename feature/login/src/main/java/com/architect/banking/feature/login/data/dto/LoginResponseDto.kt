package com.architect.banking.feature.login.data.dto

import kotlinx.serialization.Serializable

/**
 * Response payload for a successful `POST /auth/login` call.
 *
 * @property userId Unique server-assigned identifier for the authenticated user.
 * @property token Short-lived JWT access token.
 * @property refreshToken Long-lived token used to obtain a new [token] via `/auth/refresh`.
 * @property expiresAt Unix epoch milliseconds at which [token] expires.
 * @property userName Display name of the authenticated user.
 * @property accountType Account tier, e.g. `"INSTITUTIONAL"` or `"RETAIL"`.
 */
@Serializable
data class LoginResponseDto(
    val userId: String,
    val token: String,
    val refreshToken: String,
    val expiresAt: Long,
    val userName: String? = null,
    val accountType: String? = null,
)

/**
 * Response payload for a successful `POST /auth/refresh` call.
 *
 * @property token New short-lived JWT access token.
 * @property refreshToken New long-lived refresh token (rotation).
 * @property expiresAt Unix epoch milliseconds at which the new [token] expires.
 */
@Serializable
data class TokenResponseDto(
    val token: String,
    val refreshToken: String,
    val expiresAt: Long,
)
