package com.architect.banking.core.domain.model

/**
 * Domain model representing a bank account.
 * Pure Kotlin — no framework dependencies.
 *
 * @property id Unique account identifier.
 * @property name Display name (e.g. "Checking Account").
 * @property balance Current available balance.
 * @property currency ISO 4217 currency code (e.g. "USD").
 * @property accountNumber Masked account number for display.
 */
data class Account(
    val id: String,
    val name: String,
    val balance: Double,
    val currency: String,
    val accountNumber: String,
)
