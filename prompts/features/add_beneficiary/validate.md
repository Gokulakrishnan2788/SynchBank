# Validate — Add Beneficiary Screen
# cat with: CONTEXT.md + features/validate.md + this file

## UI Pixel Accuracy (open Add_Beneficiary.png and verify EVERY item)

### Header
[ ] Back arrow (←) left — 24dp navy, 48dp tap target
[ ] "Architect" title centered — 18sp SemiBold navy
[ ] Bell icon right + Avatar icon right of bell
[ ] White header bg, no shadow

### Page Header
[ ] "SECURITY & IDENTITY" — 11sp Bold Uppercase TealAccent, marginTop 24dp
[ ] "Add Beneficiary" — 28sp Bold #1A1A1A, marginTop 8dp
[ ] Subtitle — 14sp Regular #6B7280, marginTop 8dp

### Form Card
[ ] White card, 12dp radius, 1dp border #F0F1F3, padding 16dp
[ ] Field labels: 11sp Bold Uppercase #9CA3AF
[ ] Field bg: #F5F6F8, 8dp radius, 1dp border #E5E7EB, height 48dp
[ ] ACCOUNT NAME placeholder: "John Doe"
[ ] ACCOUNT NAME helper: "Must match the legal name on the destination account."
[ ] BANK NAME is dropdown with chevron right, placeholder "Select Institution"
[ ] BANK NAME dropdown opens bottom sheet with institution list
[ ] ACCOUNT NUMBER: numeric keyboard, placeholder "0000000000"
[ ] NICKNAME field: placeholder "e.g. Rent Payment", helper text below
[ ] 16dp gap between all fields
[ ] Active field border: 1.5dp TealAccent
[ ] Error field border: 1.5dp Red + error text below field

### Verification Card
[ ] White card, 12dp radius, 1dp border #F0F1F3, padding 16dp
[ ] Teal shield icon in TealLight square container (48dp × 48dp, 12dp radius)
[ ] "Secure Verification" 16sp SemiBold
[ ] Description text 13sp #6B7280
[ ] Decorative illustration partially visible right side (overflow clipped)

### Save Button
[ ] Full width, NavyPrimary bg, white text "Save Beneficiary" 16sp SemiBold
[ ] Height 56dp, 12dp radius
[ ] Shows loading spinner when isSubmitting=true

### Bottom Nav
[ ] PAYMENTS tab selected (dark navy pill) — same as Transfer screen

## Behaviour
[ ] Back arrow / system back → popBackStack() → returns to Transfer screen exactly
[ ] NOT navigating to home or any other tab
[ ] Save with blank Account Name → inline error below field
[ ] Save with no bank selected → inline error below field
[ ] Save with non-numeric account number → inline error
[ ] Save with valid data → Toast "Beneficiary added successfully" + popBackStack
[ ] API failure → Toast error message
[ ] All validations show inline per-field errors — not a single toast for all
[ ] BANK NAME dropdown shows full list of institutions from mock JSON

## Architecture
[ ] AddBeneficiaryScreen uses SDUIRenderer only — zero hardcoded layout
[ ] ScreenLoadingBox shown while state.isLoading || state.screenModel == null
[ ] AddBeneficiaryViewModel extends BaseViewModel
[ ] Lives in :feature:transfer — NOT a separate feature module
[ ] Uses TransferRepository — NOT a new repository
[ ] All validation in AddBeneficiaryUseCase only — never in ViewModel
[ ] BackHandler connected to NavigateBack intent

## SDUI
[ ] add_beneficiary_screen.json in assets/mock/screens/
[ ] NAVIGATE_BACK action in JSON wired to back arrow
[ ] SUBMIT_BENEFICIARY action in JSON wired to Save button
[ ] ADD_BENEFICIARY_FORM renders all 4 fields with correct inputTypes
[ ] VERIFICATION_CARD renders with shield icon and description

## Build
[ ] ./gradlew :feature:transfer:compileDebugKotlin — zero errors
[ ] ./gradlew :feature:transfer:testDebugUnitTest — zero failures

## Output format
FAIL: file + line + fix | PASS: ✅ | Summary: X/Y passed
