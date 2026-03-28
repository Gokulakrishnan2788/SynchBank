# Payments (Transfer) — Domain Layer
# cat with: CONTEXT.md + contract/domain_contract.md + this file

## Generate these files ONLY
- TransferRepository.kt (interface)
- TransferRepositoryImpl.kt
- SubmitTransferUseCase.kt
- GetBeneficiariesUseCase.kt
- GetSourceAccountsUseCase.kt
- AddBeneficiaryUseCase.kt
- TransferMapper.kt
- TransferResult.kt / Beneficiary.kt / NewBeneficiary.kt / SourceAccount.kt / BeneficiaryGrid.kt

## TransferRepository Interface
interface TransferRepository {
    suspend fun getSourceAccounts(): Result<List<SourceAccount>>
    suspend fun getBeneficiaries(): Result<BeneficiaryGrid>
    suspend fun submitTransfer(sourceAccountId: String, beneficiaryId: String, amount: Double, note: String?): Result<TransferResult>
    suspend fun addBeneficiary(accountName: String, bankName: String, accountNumber: String, nickname: String?): Result<AddBeneficiaryResult>
}

## UseCases

### SubmitTransferUseCase
Params: data class Params(val sourceAccountId: String, val beneficiaryId: String, val amount: Double, val note: String?)
Validation:
  - sourceAccountId not blank
  - beneficiaryId not blank
  - amount > 0.0
  - amount <= 250000.0 (daily limit)
Returns: Result<TransferResult>

### AddBeneficiaryUseCase
Params: data class Params(val accountName: String, val bankName: String, val accountNumber: String, val nickname: String?)
Validation:
  - accountName not blank
  - bankName not blank
  - accountNumber not blank, must be numeric, length 8-12
Returns: Result<AddBeneficiaryResult>

## Domain Models
data class SourceAccount(val id: String, val name: String, val maskedNumber: String, val balance: String, val balanceRaw: Double)
data class BeneficiaryGrid(val columnCount: Int, val beneficiaries: List<Beneficiary>)
data class Beneficiary(val id: String, val name: String, val subtitle: String, val avatarUrl: String?, val avatarAsset: String?, val tileBgColor: String?, val tileImageUrl: String?, val isSelected: Boolean)
data class TransferResult(val transferId: String, val status: String, val message: String, val referenceNumber: String)
data class AddBeneficiaryResult(val beneficiaryId: String, val status: String, val message: String)
data class NewBeneficiary(val accountName: String, val bankName: String, val accountNumber: String, val nickname: String?)

## Rules
- All validation in UseCases only — never in ViewModel or Repository
- TransferRepositoryImpl in :feature:transfer
- Interface in :core:domain
- Pure Kotlin domain models
