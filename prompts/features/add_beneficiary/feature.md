# Feature: Add Beneficiary Screen (Sub-screen of Payments Tab)
# Master overview — load alongside specific layer files
# Screenshot: app/src/main/assets/screenshots/Add_Beneficiary.png
# Claude Code MUST open Add_Beneficiary.png before generating any UI code.

## Screen Reference
Route: "add_beneficiary" — nested inside :feature:transfer nav graph
Entry point: tapping "NEW RECIPIENT" tile in Transfer screen beneficiary grid
Exit: back button / back press → popBackStack() → returns to Transfer screen
Module: :feature:transfer (same module as payments — NOT a separate module)

## Pixel-Perfect UI Description (from Add_Beneficiary.png)

### Header — ARCH_HEADER
- Back arrow (←) left — 24dp, navy #1B2A5E
- "Architect" title centered — 18sp SemiBold #1B2A5E
- Bell icon (notification) right — outlined grey
- Avatar icon right of bell — circular ~32dp, user avatar
- White header bg, no elevation

### Page Header Section
- "SECURITY & IDENTITY" — 11sp uppercase TealAccent #00C8A0, marginTop 24dp
- "Add Beneficiary" — 28sp Bold #1A1A1A, marginTop 8dp
- Subtitle: "Securely save account details for faster recurring transfers and payments."
  — 14sp Regular #6B7280, marginTop 8dp, lineHeight 20sp

### Form Card — ADD_BENEFICIARY_FORM
- White card: 12dp cornerRadius, 16dp padding, margin horizontal 16dp
- marginTop 28dp from subtitle
- Background: #FFFFFF, border: 1dp #F0F1F3

#### Field 1 — ACCOUNT NAME
- Label: "ACCOUNT NAME" — 11sp Uppercase #6B7280
- Input field: bg #F5F6F8, 8dp radius, 1dp border #E5E7EB
  - Placeholder: "John Doe" — 15sp #9CA3AF
  - Height: 48dp, horizontal padding 12dp
- Helper text: "Must match the legal name on the destination account."
  — 12sp #9CA3AF, marginTop 4dp

#### Field 2 — BANK NAME
- Label: "BANK NAME" — 11sp Uppercase #6B7280, marginTop 16dp
- Dropdown field: bg #F5F6F8, 8dp radius, 1dp border #E5E7EB
  - "Select Institution" — 15sp #9CA3AF left
  - Chevron down icon right — 20dp #9CA3AF
  - Height: 48dp, horizontal padding 12dp
- Options: Chase, Bank of America, Wells Fargo, Citibank, Goldman Sachs, Morgan Stanley, Other

#### Field 3 — ACCOUNT NUMBER
- Label: "ACCOUNT NUMBER" — 11sp Uppercase #6B7280, marginTop 16dp
- Input field: bg #F5F6F8, 8dp radius, 1dp border #E5E7EB
  - Placeholder: "0000000000" — 15sp #9CA3AF
  - inputType: NUMBER (numeric keyboard)
  - Height: 48dp, horizontal padding 12dp

#### Field 4 — NICKNAME (OPTIONAL)
- Label: "NICKNAME (OPTIONAL)" — 11sp Uppercase #6B7280, marginTop 16dp
- Input field: bg #F5F6F8, 8dp radius, 1dp border #E5E7EB
  - Placeholder: "e.g. Rent Payment" — 15sp #9CA3AF
  - Height: 48dp, horizontal padding 12dp
- Helper text: "A short name to help you identify this contact."
  — 12sp #9CA3AF, marginTop 4dp

### Verification Card — VERIFICATION_CARD
- White card: 12dp cornerRadius, 16dp padding, margin horizontal 16dp
- marginTop 24dp from form card
- Layout: Row — icon left + text right + illustration partially visible far right

#### Icon
- Square container: 48dp × 48dp, 12dp radius, bg TealLight #E0F7F4
- Shield checkmark icon: 28dp, TealAccent #00C8A0

#### Text block
- "Secure Verification" — 16sp SemiBold #1A1A1A
- "Architect verifies all beneficiary accounts against global banking standards to prevent fraud."
  — 13sp Regular #6B7280, marginTop 4dp, lineHeight 18sp

#### Illustration
- Abstract decorative illustration — partially visible on right edge of card (clipped)
- Asset: ic_verification_illustration

### Save Button
- Full width "Save Beneficiary" — NavyPrimary bg #1B2A5E, white text 16sp SemiBold
- Height: 56dp, 12dp cornerRadius, marginTop 24dp, horizontal margin 16dp

## Color Tokens (from Add_Beneficiary.png)
NavyPrimary:     #1B2A5E  → save button bg, back arrow, title
TealAccent:      #00C8A0  → SECURITY & IDENTITY label, shield icon
TealLight:       #E0F7F4  → shield icon container bg
BackgroundGrey:  #F5F6F8  → screen bg, input field bg
White:           #FFFFFF  → form card, verification card
TextPrimary:     #1A1A1A  → page title, "Secure Verification" label
TextSecondary:   #6B7280  → subtitle, description text
TextTertiary:    #9CA3AF  → field labels, placeholders, helper text
CardBorder:      #F0F1F3  → card borders
FieldBorder:     #E5E7EB  → input field borders

## Module
:feature:transfer — Add Beneficiary is part of transfer, NOT a separate module

## Files to generate (all 7 layers — see individual .md files in this folder)
API:     AddBeneficiaryRequestDto, AddBeneficiaryResponseDto (already in payments/api.md)
Domain:  AddBeneficiaryUseCase (already in payments/domain.md)
UI:      AddBeneficiaryScreen.kt, AddBeneficiaryViewModel.kt, AddBeneficiaryState.kt
         assets/mock/screens/add_beneficiary_screen.json
Tests:   AddBeneficiaryViewModelTest, AddBeneficiaryUseCaseTest (in payments/test.md)
Validate: add_beneficiary/validate.md

## Feature Rules
- This is a sub-screen — NOT a bottom nav tab
- Back press → popBackStack() to Transfer screen — never navigate to home or other tabs
- After successful save → Toast "Beneficiary added successfully" + popBackStack()
- BANK NAME is a dropdown — shows bottom sheet or dialog picker with institution list
- All field validations in AddBeneficiaryUseCase ONLY — never in ViewModel
- Screen uses ScreenLoadingBox while screenModel is null
- Screen is 100% SDUI — zero hardcoded Composable layout
- Every unimplemented action → Toast "Not implemented yet"
