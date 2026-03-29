package com.architect.banking.feature.payments.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In-memory singleton store for beneficiaries.
 * Starts with the default 3 beneficiaries; new ones are appended via [add].
 * [TransferViewModel] collects [beneficiaries] to keep the grid live.
 */
@Singleton
class BeneficiaryStore @Inject constructor() {

    private val _beneficiaries = MutableStateFlow(defaultBeneficiaries())
    val beneficiaries: StateFlow<List<Beneficiary>> = _beneficiaries

    fun add(name: String, bankName: String, accountNumber: String, nickname: String?) {
        val displayName = nickname?.takeIf { it.isNotBlank() } ?: name
        val subtitle = bankName.uppercase()
        val id = "ben_${System.currentTimeMillis()}"
        _beneficiaries.update { current ->
            current + Beneficiary(id = id, name = displayName, subtitle = subtitle, isSelected = false)
        }
    }

    fun getAll(): List<Beneficiary> = _beneficiaries.value

    private fun defaultBeneficiaries(): List<Beneficiary> = listOf(
        Beneficiary("ben_001", "Alex Sterling", "STUDIO ALPHA", false),
        Beneficiary("ben_002", "Mila Novak", "NOVAK & CO.", true),
        Beneficiary("ben_003", "Julian Drax", "PERSONAL", false),
    )
}
