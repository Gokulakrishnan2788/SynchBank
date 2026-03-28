package com.architect.banking.feature.payments.domain

data class Beneficiary(
    val id: String,
    val name: String,
    val subtitle: String,
    val isSelected: Boolean,
)

data class BeneficiaryGrid(
    val columnCount: Int,
    val beneficiaries: List<Beneficiary>,
)

data class TransferResult(
    val transferId: String,
    val status: String,
    val message: String,
    val referenceNumber: String = "",
)

data class AddBeneficiaryResult(
    val beneficiaryId: String,
    val status: String,
    val message: String,
)
