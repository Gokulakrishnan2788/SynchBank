package com.architect.banking.core.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a persisted user session.
 * Maps to the "session_table" in the local SQLite database.
 *
 * @property userId Unique identifier of the authenticated user (primary key).
 * @property token Current bearer token for API requests.
 * @property refreshToken Token used to obtain a new [token] when expired.
 * @property expiresAt Unix timestamp (ms) when [token] expires.
 * @property createdAt Unix timestamp (ms) when this session was stored locally.
 */
@Entity(tableName = "session_table")
data class SessionEntity(
    @PrimaryKey
    val userId: String,
    val token: String,
    val refreshToken: String,
    val expiresAt: Long,
    val createdAt: Long = System.currentTimeMillis(),
)
