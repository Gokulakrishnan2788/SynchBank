# Payments (Transfer + Add Beneficiary) — UI Layer
# cat with: CONTEXT.md + contract/mvi.md + contract/sdui_contract.md
#           + contract/renderer.md + composite/composite_components.md + this file
# Screenshots:
#   app/src/main/assets/screenshots/Payments.png
#   app/src/main/assets/screenshots/Add_Beneficiary.png
# Claude Code MUST open BOTH screenshots before writing any code.

## Generate these files ONLY
- TransferScreen.kt
- AddBeneficiaryScreen.kt
- TransferViewModel.kt
- AddBeneficiaryViewModel.kt
- TransferState.kt
- AddBeneficiaryState.kt
- TransferModule.kt
- assets/mock/screens/transfer_screen.json
- assets/mock/screens/add_beneficiary_screen.json

## TransferScreen.kt
- Route: "payments" inside MainScreen NavHost
- Uses SDUIRenderer ONLY
- init → TransferIntent.LoadScreen
- AddNewBeneficiary effect → navController.navigate("add_beneficiary")
- ShowToast → Android Toast
- ShowSuccess → Toast with reference number
- Has @Preview

## AddBeneficiaryScreen.kt
- Route: "add_beneficiary" — nested inside payments nav graph
- Uses SDUIRenderer ONLY
- Back press / NavigateBack effect → navController.popBackStack()
- ShowSuccess → Toast then popBackStack
- Has @Preview

## SDUI JSON — transfer_screen.json
{
  "screenId": "transfer",
  "version": "1.0",
  "metadata": { "title": "Transfer", "analyticsTag": "transfer_screen" },
  "layout": { "type": "SCROLL", "padding": { "horizontal": 20, "vertical": 0 } },
  "components": [
    {
      "id": "header",
      "type": "ARCH_HEADER",
      "props": { "logoAsset": "ic_architect_avatar", "title": "Architect", "showSearch": false, "showNotification": true, "showBack": false, "showAvatar": false }
    },
    { "id": "spacer_1", "type": "SPACER", "props": { "height": 16 } },
    { "id": "step_label", "type": "TEXT", "props": { "text": "STEP 01", "style": "LABEL", "color": "TealAccent" } },
    {
      "id": "title_row",
      "type": "ROW",
      "props": {
        "arrangement": "SPACE_BETWEEN",
        "children": [
          { "id": "transfer_title", "type": "TEXT", "props": { "text": "Transfer", "style": "DISPLAY" } },
          { "id": "confirmation_label", "type": "TEXT", "props": { "text": "Confirmation", "style": "BODY", "color": "TextTertiary" } }
        ]
      }
    },
    { "id": "spacer_2", "type": "SPACER", "props": { "height": 4 } },
    { "id": "progress_bar", "type": "DIVIDER", "props": { "thickness": 3, "color": "TealAccent", "widthFraction": 0.4 } },
    { "id": "spacer_3", "type": "SPACER", "props": { "height": 24 } },
    { "id": "source_label", "type": "TEXT", "props": { "text": "SELECT SOURCE ACCOUNT", "style": "LABEL", "color": "TextSecondary" } },
    { "id": "spacer_4", "type": "SPACER", "props": { "height": 8 } },
    {
      "id": "source_selector",
      "type": "SOURCE_ACCOUNT_SELECTOR",
      "props": {
        "selectedAccountName": "Wealth Management — •••• 8829",
        "availableBalance": "$142,850.00",
        "balanceColor": "TealAccent",
        "accounts": [
          { "id": "acc_001", "name": "Elite Checking", "maskedNumber": "•••• 8821", "balance": "$42,905.12" },
          { "id": "acc_003", "name": "Wealth Management", "maskedNumber": "•••• 8829", "balance": "$142,850.00" }
        ]
      },
      "action": "ACCOUNT_SELECTED"
    },
    { "id": "spacer_5", "type": "SPACER", "props": { "height": 20 } },
    { "id": "amount_label", "type": "TEXT", "props": { "text": "TRANSFER AMOUNT", "style": "LABEL", "color": "TextSecondary" } },
    { "id": "spacer_6", "type": "SPACER", "props": { "height": 8 } },
    {
      "id": "amount_input",
      "type": "AMOUNT_INPUT_CARD",
      "props": {
        "currencySymbol": "$",
        "hint": "0.00",
        "quickAmounts": [
          { "label": "+$1,000", "value": 1000 },
          { "label": "+$5,000", "value": 5000 },
          { "label": "+$10,000", "value": 10000 }
        ]
      },
      "action": "AMOUNT_CHANGED"
    },
    { "id": "spacer_7", "type": "SPACER", "props": { "height": 20 } },
    { "id": "note_label", "type": "TEXT", "props": { "text": "ADD NOTE (OPTIONAL)", "style": "LABEL", "color": "TextSecondary" } },
    { "id": "spacer_8", "type": "SPACER", "props": { "height": 8 } },
    {
      "id": "note_field",
      "type": "TEXT_FIELD",
      "props": { "label": "", "hint": "e.g. Q3 Studio Renovation...", "inputType": "TEXT", "required": false, "validations": [] },
      "action": "NOTE_CHANGED"
    },
    { "id": "spacer_9", "type": "SPACER", "props": { "height": 28 } },
    {
      "id": "confirm_btn",
      "type": "BUTTON",
      "props": { "label": "Confirm & Proceed", "style": "PRIMARY", "loading": false },
      "action": "CONFIRM_PROCEED"
    },
    { "id": "spacer_10", "type": "SPACER", "props": { "height": 28 } },
    { "id": "beneficiary_label", "type": "TEXT", "props": { "text": "BENEFICIARY", "style": "LABEL", "color": "TextSecondary" } },
    { "id": "spacer_11", "type": "SPACER", "props": { "height": 4 } },
    {
      "id": "beneficiary_header",
      "type": "SECTION_HEADER_ROW",
      "props": { "title": "Recent", "trailingLabel": "VIEW ALL", "trailingColor": "TealAccent", "trailingAction": "VIEW_ALL" }
    },
    { "id": "spacer_12", "type": "SPACER", "props": { "height": 12 } },
    {
      "id": "beneficiary_grid",
      "type": "BENEFICIARY_GRID",
      "props": {
        "columnCount": 2,
        "beneficiaries": [
          { "id": "ben_001", "name": "Alex Sterling", "subtitle": "STUDIO ALPHA", "avatarAsset": "avatar_alex", "isSelected": false },
          { "id": "ben_002", "name": "Mila Novak", "subtitle": "NOVAK & CO.", "avatarAsset": "avatar_mila", "isSelected": true },
          { "id": "ben_003", "name": "Julian Drax", "subtitle": "PERSONAL", "avatarAsset": "avatar_julian", "isSelected": false }
        ],
        "showAddNew": true
      },
      "action": "BENEFICIARY_SELECTED"
    },
    { "id": "spacer_13", "type": "SPACER", "props": { "height": 20 } },
    {
      "id": "transfer_limit",
      "type": "TRANSFER_LIMIT_BANNER",
      "props": { "label": "Transfer Limit", "amount": "$250,000", "suffix": "DAILY REMAINING", "bgColor": "TealAccent", "illustrationAsset": "ic_bank_building" }
    },
    { "id": "spacer_end", "type": "SPACER", "props": { "height": 80 } }
  ],
  "actions": {
    "ACCOUNT_SELECTED":  { "type": "API_CALL", "endpoint": "/transfer/account", "method": "POST" },
    "AMOUNT_CHANGED":    { "type": "API_CALL", "endpoint": "/transfer/amount", "method": "POST" },
    "NOTE_CHANGED":      { "type": "API_CALL", "endpoint": "/transfer/note", "method": "POST" },
    "CONFIRM_PROCEED":   { "type": "API_CALL", "endpoint": "/transfer", "method": "POST",
                           "onSuccess": { "type": "SHOW_DIALOG", "params": { "message": "Transfer successful!" } },
                           "onError":   { "type": "SHOW_DIALOG", "params": { "message": "Transfer failed. Please try again." } } },
    "BENEFICIARY_SELECTED": { "type": "API_CALL", "endpoint": "/transfer/beneficiary", "method": "POST" },
    "ADD_BENEFICIARY":   { "type": "NAVIGATE", "destination": "add_beneficiary" },
    "VIEW_ALL":          { "type": "SHOW_DIALOG", "params": { "message": "Not implemented yet" } }
  }
}

## SDUI JSON — add_beneficiary_screen.json
{
  "screenId": "add_beneficiary",
  "version": "1.0",
  "metadata": { "title": "Add Beneficiary", "analyticsTag": "add_beneficiary_screen" },
  "layout": { "type": "SCROLL", "padding": { "horizontal": 20, "vertical": 0 } },
  "components": [
    {
      "id": "header",
      "type": "ARCH_HEADER",
      "props": { "logoAsset": "ic_architect_avatar", "title": "Architect", "showSearch": false, "showNotification": true, "showBack": true, "showAvatar": true, "backAction": "NAVIGATE_BACK" }
    },
    { "id": "spacer_1", "type": "SPACER", "props": { "height": 24 } },
    { "id": "security_label", "type": "TEXT", "props": { "text": "SECURITY & IDENTITY", "style": "LABEL", "color": "TealAccent" } },
    { "id": "spacer_2", "type": "SPACER", "props": { "height": 8 } },
    { "id": "page_title", "type": "TEXT", "props": { "text": "Add Beneficiary", "style": "HEADING", "color": "TextPrimary" } },
    { "id": "spacer_3", "type": "SPACER", "props": { "height": 8 } },
    { "id": "page_subtitle", "type": "TEXT", "props": { "text": "Securely save account details for faster recurring transfers and payments.", "style": "BODY", "color": "TextSecondary" } },
    { "id": "spacer_4", "type": "SPACER", "props": { "height": 28 } },
    {
      "id": "beneficiary_form",
      "type": "ADD_BENEFICIARY_FORM",
      "props": {
        "fields": [
          { "id": "account_name", "label": "ACCOUNT NAME", "hint": "John Doe", "helperText": "Must match the legal name on the destination account.", "inputType": "TEXT" },
          { "id": "bank_name", "label": "BANK NAME", "hint": "Select Institution", "helperText": null, "inputType": "DROPDOWN", "options": ["Chase", "Bank of America", "Wells Fargo", "Citibank", "Goldman Sachs", "Morgan Stanley", "Other"] },
          { "id": "account_number", "label": "ACCOUNT NUMBER", "hint": "0000000000", "helperText": null, "inputType": "NUMBER" },
          { "id": "nickname", "label": "NICKNAME (OPTIONAL)", "hint": "e.g. Rent Payment", "helperText": "A short name to help you identify this contact.", "inputType": "TEXT" }
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
    "SUBMIT_BENEFICIARY": { "type": "API_CALL", "endpoint": "/beneficiary", "method": "POST",
                            "onSuccess": { "type": "POP" },
                            "onError":   { "type": "SHOW_DIALOG", "params": { "message": "Failed to add beneficiary." } } }
  }
}
