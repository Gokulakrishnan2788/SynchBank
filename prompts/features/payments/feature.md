# Feature: Payments Tab (Transfer Screen + Add Beneficiary Sub-screen)
# Screenshot: app/src/main/assets/screenshots/Payments.png
#             app/src/main/assets/screenshots/Add_Beneficiary.png
# Claude Code MUST open both screenshots before generating any UI code.

## Screen 1 — Transfer Screen
Route: "payments" inside MainScreen NavHost

### Header
- ARCH_HEADER: showSearch=false, showNotification=true, showBack=false

### Step Indicator
- "STEP 01" — teal (#00C8A0) 11sp uppercase label
- "Transfer" — 28sp bold #1B2A5E (large, left)
- Progress bar — thin teal line ~40% width under "STEP 01"
- "Confirmation" — 14sp grey right-aligned (step 2 label, inactive)

### Source Account Selector
- "SELECT SOURCE ACCOUNT" — 11sp uppercase grey label
- SOURCE_ACCOUNT_SELECTOR card:
  - White card 12dp radius, subtle shadow
  - "Wealth Management — •••• 8829" 16sp medium left + chevron right
  - "Available Balance" grey 12sp + "$142,850.00" teal 14sp bold

### Transfer Amount
- "TRANSFER AMOUNT" — 11sp uppercase grey label
- AMOUNT_INPUT_CARD:
  - White card 12dp radius
  - "$" symbol 24sp grey left + "0.00" 40sp ghost text centre-left
  - Three chips row: "+$1,000" "+$5,000" "+$10,000"
    - Chip: outlined 1dp #E5E7EB border, 20dp radius, 14sp #1A1A1A

### Add Note
- "ADD NOTE (OPTIONAL)" — 11sp uppercase grey
- Single outlined text field, placeholder "e.g. Q3 Studio Renovation..."
- Border: 1dp #E5E7EB, 12dp radius, bg white

### Confirm Button
- Full width "Confirm & Proceed ›" — NavyPrimary bg, white 16sp semibold, 12dp radius, 56dp height

### Beneficiary Section
- "BENEFICIARY" — 11sp uppercase grey small label
- SECTION_HEADER_ROW: "Recent" bold 20sp + "VIEW ALL" TealAccent right
- BENEFICIARY_GRID:
  - columnCount: 2 (dynamic from server)
  - Tile size: equal, white card 12dp radius
  - Avatar: circular ~56dp, centered top of tile
  - Name: 14sp bold center
  - Subtitle: 12sp grey center
  - Selected tile: NavyPrimary bg, white text, teal checkmark overlay on avatar
  - New Recipient tile: dashed border 1dp #C5C9D0, + icon center, "NEW RECIPIENT" 12sp grey
  - Tile padding: 12dp

### Transfer Limit Banner
- TRANSFER_LIMIT_BANNER: teal bg, "Transfer Limit" + "$250,000 DAILY REMAINING" + bank illustration

## Screen 2 — Add Beneficiary (Sub-screen)
Route: "add_beneficiary" — replaces payments tab content (not a dialog)
Back button → returns to payments/transfer screen

### Header
- ARCH_HEADER: showBack=true, showNotification=true, showAvatar=true
- Back arrow left, "Architect" title, Bell + avatar right

### Page Header
- "SECURITY & IDENTITY" — teal 11sp uppercase label
- "Add Beneficiary" — 28sp bold #1A1A1A
- "Securely save account details for faster recurring transfers and payments." — 14sp grey

### Form Card
- White card, 12dp radius, 16dp padding
- ADD_BENEFICIARY_FORM with 4 fields:
  1. ACCOUNT NAME / "John Doe" / helper: "Must match the legal name on the destination account."
  2. BANK NAME / dropdown "Select Institution" / chevron right
  3. ACCOUNT NUMBER / "0000000000" / NUMBER input
  4. NICKNAME (OPTIONAL) / "e.g. Rent Payment" / helper: "A short name to help you identify this contact."
- Field style: label 11sp uppercase grey, input bg #F5F6F8, 8dp radius, 1dp border #E5E7EB

### Verification Card
- VERIFICATION_CARD:
  - White card 12dp radius
  - Teal rounded square icon (shield checkmark) left
  - "Secure Verification" 16sp bold
  - "Architect verifies all beneficiary accounts against global banking standards to prevent fraud." 14sp grey
  - Abstract illustration right side (decorative, partially visible)

## Color Tokens
NavyPrimary:    #1B2A5E
TealAccent:     #00C8A0
BackgroundGrey: #F5F6F8
White:          #FFFFFF
TextSecondary:  #6B7280
CardBorder:     #E5E7EB
DashedBorder:   #C5C9D0

## Module: :feature:transfer
## Files to generate:
API:     TransferApiService, TransferDto, BeneficiaryDto
         assets/mock/api/transfer_beneficiaries.json
         assets/mock/api/transfer_submit.json
         assets/mock/api/add_beneficiary.json
Domain:  SubmitTransferUseCase, GetBeneficiariesUseCase, GetSourceAccountsUseCase,
         AddBeneficiaryUseCase, TransferRepository (interface+impl), TransferMapper,
         TransferResult, Beneficiary, NewBeneficiary (domain models)
UI:      TransferScreen, AddBeneficiaryScreen,
         TransferViewModel, AddBeneficiaryViewModel,
         TransferState/Intent/Effect, AddBeneficiaryState/Intent/Effect,
         TransferModule
         assets/mock/screens/transfer_screen.json
         assets/mock/screens/add_beneficiary_screen.json
Tests:   TransferViewModelTest, AddBeneficiaryViewModelTest,
         SubmitTransferUseCaseTest, AddBeneficiaryUseCaseTest

## Feature Rules
- Add Beneficiary is a sub-screen within payments tab — NOT a bottom nav tab
- Back from Add Beneficiary → TransferScreen (popBackStack)
- Beneficiary grid column count from server JSON — render dynamically
- Every unimplemented tap → Toast "Not implemented yet"
- Confirm & Proceed → validate → submit → show success toast or navigate to confirmation
- All card/tile themes (bg, image) from server JSON props
