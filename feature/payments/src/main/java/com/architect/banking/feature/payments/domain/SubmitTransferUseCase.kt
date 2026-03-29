package com.architect.banking.feature.payments.domain

import com.architect.banking.core.domain.Result
import javax.inject.Inject

class SubmitTransferUseCase @Inject constructor(
    private val repository: TransferRepository,
) {
    data class Params(
        val sourceAccountId: String,
        val beneficiaryId: String,
        val amount: Double,
        val note: String?,
    )

    suspend operator fun invoke(params: Params): Result<TransferResult> {
        if (params.sourceAccountId.isBlank()) {
            return Result.Error(code = "VALIDATION_ERROR", message = "Please select a source account.")
        }
        if (params.beneficiaryId.isBlank()) {
            return Result.Error(code = "VALIDATION_ERROR", message = "Please select a beneficiary.")
        }
        if (params.amount <= 0.0) {
            return Result.Error(code = "VALIDATION_ERROR", message = "Please enter a valid transfer amount.")
        }
        if (params.amount > 250000.0) {
            return Result.Error(
                code = "VALIDATION_ERROR",
                message = "Amount exceeds daily transfer limit of \$250,000.",
            )
        }
        return repository.submitTransfer(
            params.sourceAccountId,
            params.beneficiaryId,
            params.amount,
            params.note,
        )
    }
}
