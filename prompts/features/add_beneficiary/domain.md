# Add Beneficiary — Domain Layer
# cat with: CONTEXT.md + contract/domain_contract.md + this file
# Note: Lives in :feature:transfer — append to TransferRepository, do NOT create new repo

## Generate these files ONLY
- AddBeneficiaryUseCase.kt  (if not already generated in payments/domain.md)
- GetBankInstitutionsUseCase.kt
- NewBeneficiary.kt  (domain model — if not exists)
- BankInstitution.kt (domain model)
- AddBeneficiaryResult.kt (domain model — if not exists)

## Append to TransferRepository interface (do NOT redefine the whole interface)
suspend fun addBeneficiary(
    accountName: String,
    bankName: String,
    accountNumber: String,
    nickname: String?
): Result<AddBeneficiaryResult>

suspend fun getBankInstitutions(): Result<List<BankInstitution>>

## AddBeneficiaryUseCase
Params:
data class Params(
    val accountName: String,
    val bankName: String,
    val accountNumber: String,
    val nickname: String?
)

Validation (ALL in UseCase — never in ViewModel):
  - accountName: must not be blank
  - accountName: max 100 chars
  - bankName: must not be blank (must be selected from list)
  - accountNumber: must not be blank
  - accountNumber: must be numeric only (Regex: ^[0-9]+$)
  - accountNumber: length must be between 8 and 12 digits
  - nickname: optional, max 50 chars if provided

Returns: Result<AddBeneficiaryResult>

## GetBankInstitutionsUseCase
Params: NoParams
Returns: Result<List<BankInstitution>>
Use: populates BANK NAME dropdown in form

## Domain Models

data class NewBeneficiary(
    val accountName: String,
    val bankName: String,
    val accountNumber: String,
    val nickname: String?
)

data class BankInstitution(
    val id: String,
    val name: String,
    val logoAsset: String?
)

data class AddBeneficiaryResult(
    val beneficiaryId: String,
    val status: String,
    val message: String
)

## Mapper additions (append to TransferMapper.kt)
fun AddBeneficiaryResponseDto.toDomain(): AddBeneficiaryResult
fun BankInstitutionDto.toDomain(): BankInstitution

## Rules
- Pure Kotlin domain models — no @Serializable, no @Entity, no Android imports
- AddBeneficiaryUseCase in :feature:transfer
- All validation logic here — never in AddBeneficiaryViewModel or repository
- Reuse TransferRepository — do NOT create AddBeneficiaryRepository
