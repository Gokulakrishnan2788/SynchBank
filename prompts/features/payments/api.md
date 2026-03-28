# Payments (Transfer) — API Layer
# cat with: CONTEXT.md + contract/api_contract.md + this file

## Generate these files ONLY
- TransferApiService.kt
- TransferRequestDto.kt / TransferResponseDto.kt
- BeneficiaryDto.kt
- AddBeneficiaryRequestDto.kt / AddBeneficiaryResponseDto.kt
- SourceAccountDto.kt
- assets/mock/api/transfer_beneficiaries.json
- assets/mock/api/transfer_submit.json
- assets/mock/api/add_beneficiary.json
- assets/mock/api/source_accounts.json

## Endpoints
GET  /transfer/source-accounts  → List<SourceAccountDto>
GET  /transfer/beneficiaries    → List<BeneficiaryDto>
POST /transfer                  → TransferResponseDto
POST /beneficiary               → AddBeneficiaryResponseDto
GET  /screens/transfer          → ScreenResponse
GET  /screens/add-beneficiary   → ScreenResponse

## DTOs

### SourceAccountDto
{ "id": String, "name": String, "maskedNumber": String, "balance": String, "balanceRaw": Double }

### BeneficiaryDto
{
  "id": String, "name": String, "subtitle": String,
  "avatarUrl": String?, "avatarAsset": String?,
  "tileBgColor": String?, "tileImageUrl": String?,
  "isSelected": Boolean
}

### TransferRequestDto
{ "sourceAccountId": String, "beneficiaryId": String, "amount": Double, "note": String? }

### TransferResponseDto
{ "transferId": String, "status": String, "message": String, "referenceNumber": String }

### AddBeneficiaryRequestDto
{ "accountName": String, "bankName": String, "accountNumber": String, "nickname": String? }

### AddBeneficiaryResponseDto
{ "beneficiaryId": String, "status": String, "message": String }

## Mock JSONs

### source_accounts.json
{
  "success": true,
  "data": [
    { "id": "acc_001", "name": "Elite Checking", "maskedNumber": "•••• 8821", "balance": "$42,905.12", "balanceRaw": 42905.12 },
    { "id": "acc_003", "name": "Wealth Management", "maskedNumber": "•••• 8829", "balance": "$142,850.00", "balanceRaw": 142850.00 }
  ]
}

### transfer_beneficiaries.json
{
  "success": true,
  "data": {
    "columnCount": 2,
    "beneficiaries": [
      { "id": "ben_001", "name": "Alex Sterling", "subtitle": "STUDIO ALPHA", "avatarAsset": "avatar_alex", "isSelected": false },
      { "id": "ben_002", "name": "Mila Novak", "subtitle": "NOVAK & CO.", "avatarAsset": "avatar_mila", "isSelected": true },
      { "id": "ben_003", "name": "Julian Drax", "subtitle": "PERSONAL", "avatarAsset": "avatar_julian", "isSelected": false }
    ]
  }
}

### transfer_submit.json
{
  "success": true,
  "data": { "transferId": "txr_001", "status": "SUCCESS", "message": "Transfer completed successfully.", "referenceNumber": "REF-20241027-001" }
}

### add_beneficiary.json
{
  "success": true,
  "data": { "beneficiaryId": "ben_004", "status": "SUCCESS", "message": "Beneficiary added successfully." }
}

## Rules
- Do NOT create new NetworkModule or Retrofit instance
- Inject TransferApiService via TransferModule.kt
