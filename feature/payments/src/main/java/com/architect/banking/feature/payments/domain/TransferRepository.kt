package com.architect.banking.feature.payments.domain

import com.architect.banking.core.domain.Result

interface TransferRepository {
    suspend fun getSourceAccounts(): Result<List<SourceAccount>>
    suspend fun getBeneficiaries(): Result<BeneficiaryGrid>
    suspend fun submitTransfer(
        sourceAccountId: String,
        beneficiaryId: String,
        amount: Double,
        note: String?,
    ): Result<TransferResult>
    suspend fun addBeneficiary(
        accountName: String,
        bankName: String,
        accountNumber: String,
        nickname: String?,
    ): Result<AddBeneficiaryResult>
}
