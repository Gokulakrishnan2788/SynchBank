package com.architect.banking.feature.payments.domain

data class SourceAccount(
    val id: String,
    val name: String,
    val maskedNumber: String,
    val balance: String,
    val balanceRaw: Double,
)
