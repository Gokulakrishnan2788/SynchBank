package com.architect.banking.core.domain.model

/**
 * Domain model representing a single transaction.
 * Pure Kotlin — no framework dependencies.
 *
 * @property id Unique transaction identifier.
 * @property amount Transaction amount (positive = credit, negative = debit).
 * @property description Human-readable transaction description.
 * @property date Unix timestamp (ms) of the transaction.
 * @property category Optional transaction category for display.
 */
data class Transaction(
    val id: String,
    val amount: Double,
    val description: String,
    val date: Long,
    val category: String? = null,
)
