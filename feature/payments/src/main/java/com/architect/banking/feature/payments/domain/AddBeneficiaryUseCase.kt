package com.architect.banking.feature.payments.domain

import com.architect.banking.core.domain.Result
import javax.inject.Inject

class AddBeneficiaryUseCase @Inject constructor(
    private val repository: TransferRepository,
) {
    data class Params(
        val accountName: String,
        val bankName: String,
        val accountNumber: String,
        val nickname: String?,
    )

    suspend operator fun invoke(params: Params): Result<AddBeneficiaryResult> {
        if (params.accountName.isBlank()) {
            return Result.Error("VALIDATION_ERROR", "Account name is required.")
        }
        if (params.bankName.isBlank()) {
            return Result.Error("VALIDATION_ERROR", "Bank name is required.")
        }
        if (params.accountNumber.isBlank() || !params.accountNumber.all { it.isDigit() }) {
            return Result.Error("VALIDATION_ERROR", "Account number must be numeric.")
        }
        if (params.accountNumber.length !in 8..12) {
            return Result.Error("VALIDATION_ERROR", "Account number must be 8\u201312 digits.")
        }
        return repository.addBeneficiary(
            params.accountName,
            params.bankName,
            params.accountNumber,
            params.nickname,
        )
    }
}
