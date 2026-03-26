package com.architect.banking.core.domain.model

/**
 * Domain model representing an authenticated user session.
 * Pure Kotlin — no Android framework, serialization, or Room annotations.
 *
 * @property userId Unique identifier of the authenticated user.
 * @property token Bearer token for API requests.
 * @property refreshToken Token used to obtain a new [token] when it expires.
 * @property expiresAt Unix timestamp (ms) when [token] expires.
 */
data class AuthSession(
    val userId: String,
    val token: String,
    val refreshToken: String,
    val expiresAt: Long,
)
