package com.architect.banking.feature.payments.domain

import com.architect.banking.core.domain.Result
import javax.inject.Inject

class GetBeneficiariesUseCase @Inject constructor(
    private val repository: TransferRepository,
) {
    suspend operator fun invoke(): Result<BeneficiaryGrid> = repository.getBeneficiaries()
}
