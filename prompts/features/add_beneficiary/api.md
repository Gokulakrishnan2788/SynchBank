# Add Beneficiary — API Layer
# cat with: CONTEXT.md + contract/api_contract.md + this file
# Note: AddBeneficiary lives in :feature:transfer — do NOT create a new module

## Generate these files ONLY (append to TransferApiService.kt — do NOT create new service)
- AddBeneficiaryRequestDto.kt  (if not already generated in payments/api.md)
- AddBeneficiaryResponseDto.kt (if not already generated in payments/api.md)
- BankInstitutionDto.kt
- assets/mock/api/add_beneficiary.json
- assets/mock/api/bank_institutions.json
- assets/mock/screens/add_beneficiary_screen.json

## Endpoint (add to TransferApiService)
POST /beneficiary               → AddBeneficiaryResponseDto
GET  /beneficiary/institutions  → List<BankInstitutionDto>
GET  /screens/add-beneficiary   → ScreenResponse (SDUI JSON)

## DTOs

### AddBeneficiaryRequestDto
@Serializable
data class AddBeneficiaryRequestDto(
    val accountName: String,
    val bankName: String,
    val accountNumber: String,
    val nickname: String? = null
)

### AddBeneficiaryResponseDto
@Serializable
data class AddBeneficiaryResponseDto(
    val beneficiaryId: String,
    val status: String,    // "SUCCESS" | "FAILED"
    val message: String
)

### BankInstitutionDto
@Serializable
data class BankInstitutionDto(
    val id: String,
    val name: String,
    val logoAsset: String? = null
)

## Mock JSON — add_beneficiary.json
{
  "success": true,
  "data": {
    "beneficiaryId": "ben_004",
    "status": "SUCCESS",
    "message": "Beneficiary added successfully."
  }
}

## Mock JSON — bank_institutions.json
{
  "success": true,
  "data": [
    { "id": "bank_001", "name": "Chase",           "logoAsset": "ic_bank_chase" },
    { "id": "bank_002", "name": "Bank of America", "logoAsset": "ic_bank_boa" },
    { "id": "bank_003", "name": "Wells Fargo",     "logoAsset": "ic_bank_wf" },
    { "id": "bank_004", "name": "Citibank",        "logoAsset": "ic_bank_citi" },
    { "id": "bank_005", "name": "Goldman Sachs",   "logoAsset": "ic_bank_gs" },
    { "id": "bank_006", "name": "Morgan Stanley",  "logoAsset": "ic_bank_ms" },
    { "id": "bank_007", "name": "Other",           "logoAsset": null }
  ]
}

## Mock JSON — add_beneficiary_screen.json
{
  "screenId": "add_beneficiary",
  "version": "1.0",
  "metadata": { "title": "Add Beneficiary", "analyticsTag": "add_beneficiary_screen" },
  "layout": { "type": "SCROLL", "padding": { "horizontal": 20, "vertical": 0 } },
  "components": [
    {
      "id": "header",
      "type": "ARCH_HEADER",
      "props": {
        "logoAsset": "ic_architect_avatar",
        "title": "Architect",
        "showSearch": false,
        "showNotification": true,
        "showBack": true,
        "showAvatar": true,
        "backAction": "NAVIGATE_BACK"
      }
    },
    { "id": "spacer_1", "type": "SPACER", "props": { "height": 24 } },
    {
      "id": "security_label",
      "type": "TEXT",
      "props": { "text": "SECURITY & IDENTITY", "style": "LABEL", "color": "TealAccent" }
    },
    { "id": "spacer_2", "type": "SPACER", "props": { "height": 8 } },
    {
      "id": "page_title",
      "type": "TEXT",
      "props": { "text": "Add Beneficiary", "style": "DISPLAY_MEDIUM", "color": "TextPrimary" }
    },
    { "id": "spacer_3", "type": "SPACER", "props": { "height": 8 } },
    {
      "id": "page_subtitle",
      "type": "TEXT",
      "props": {
        "text": "Securely save account details for faster recurring transfers and payments.",
        "style": "BODY",
        "color": "TextSecondary"
      }
    },
    { "id": "spacer_4", "type": "SPACER", "props": { "height": 28 } },
    {
      "id": "beneficiary_form",
      "type": "ADD_BENEFICIARY_FORM",
      "props": {
        "fields": [
          {
            "id": "account_name",
            "label": "ACCOUNT NAME",
            "hint": "John Doe",
            "helperText": "Must match the legal name on the destination account.",
            "inputType": "TEXT",
            "options": null
          },
          {
            "id": "bank_name",
            "label": "BANK NAME",
            "hint": "Select Institution",
            "helperText": null,
            "inputType": "DROPDOWN",
            "options": ["Chase", "Bank of America", "Wells Fargo", "Citibank", "Goldman Sachs", "Morgan Stanley", "Other"]
          },
          {
            "id": "account_number",
            "label": "ACCOUNT NUMBER",
            "hint": "0000000000",
            "helperText": null,
            "inputType": "NUMBER",
            "options": null
          },
          {
            "id": "nickname",
            "label": "NICKNAME (OPTIONAL)",
            "hint": "e.g. Rent Payment",
            "helperText": "A short name to help you identify this contact.",
            "inputType": "TEXT",
            "options": null
          }
        ]
      }
    },
    { "id": "spacer_5", "type": "SPACER", "props": { "height": 24 } },
    {
      "id": "verification_card",
      "type": "VERIFICATION_CARD",
      "props": {
        "iconAsset": "ic_shield_check",
        "iconBgColor": "TealLight",
        "title": "Secure Verification",
        "description": "Architect verifies all beneficiary accounts against global banking standards to prevent fraud.",
        "illustrationAsset": "ic_verification_illustration"
      }
    },
    { "id": "spacer_6", "type": "SPACER", "props": { "height": 24 } },
    {
      "id": "save_btn",
      "type": "BUTTON",
      "props": { "label": "Save Beneficiary", "style": "PRIMARY", "loading": false },
      "action": "SUBMIT_BENEFICIARY"
    },
    { "id": "spacer_end", "type": "SPACER", "props": { "height": 80 } }
  ],
  "actions": {
    "NAVIGATE_BACK":      { "type": "POP" },
    "SUBMIT_BENEFICIARY": {
      "type": "API_CALL",
      "endpoint": "/beneficiary",
      "method": "POST",
      "onSuccess": { "type": "POP" },
      "onError":   { "type": "SHOW_DIALOG", "params": { "message": "Failed to add beneficiary. Please try again." } }
    }
  }
}

## Rules
- Add endpoints to existing TransferApiService — do NOT create AddBeneficiaryApiService
- Do NOT create new NetworkModule or Retrofit instance
- All mock JSONs in assets/mock/api/ and assets/mock/screens/
