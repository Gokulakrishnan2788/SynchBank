# Validate — Payments Tab (Transfer + Add Beneficiary)
# cat with: CONTEXT.md + features/validate.md + this file

## UI Pixel Accuracy (open Payments.png and Add_Beneficiary.png and verify)

### Transfer Screen
[ ] STEP 01 label: TealAccent 11sp uppercase
[ ] "Transfer" title: 28sp bold navy, same row as "Confirmation" grey right
[ ] Progress bar: thin teal line ~40% width
[ ] Source selector card: white 12dp radius, chevron right, teal balance amount
[ ] Amount input: "$" symbol + large ghost "0.00" ~40sp + 3 outlined chips below
[ ] Note field: outlined, placeholder text, 12dp radius
[ ] Confirm button: full width, NavyPrimary bg, 56dp height, 12dp radius
[ ] Beneficiary grid: 2 columns, equal tile size
[ ] Selected tile: NavyPrimary bg, white text, teal checkmark on avatar
[ ] New Recipient tile: dashed border, + icon center
[ ] Transfer limit banner: TealAccent bg, bold amount, "DAILY REMAINING" suffix

### Add Beneficiary Screen
[ ] Back arrow in header left, avatar icon right
[ ] "SECURITY & IDENTITY" teal uppercase label
[ ] "Add Beneficiary" 28sp bold
[ ] Form card: white, 12dp radius, fields have grey bg (#F5F6F8)
[ ] Bank Name field is a DROPDOWN with "Select Institution"
[ ] Verification card: teal shield icon, illustration partially visible right

## Behaviour
[ ] Add New Recipient tile → opens Add Beneficiary screen (not dialog)
[ ] Back from Add Beneficiary → returns to Transfer screen
[ ] Beneficiary selection → tile bg changes to NavyPrimary, others reset
[ ] columnCount from server JSON drives grid layout dynamically
[ ] All unimplemented actions → Toast "Not implemented yet"
[ ] Amount chips accumulate (each tap adds to current amount)
[ ] Confirm & Proceed → validates → submits → shows success toast

## Architecture
[ ] TransferScreen uses SDUIRenderer only
[ ] AddBeneficiaryScreen uses SDUIRenderer only
[ ] Add Beneficiary route nested inside payments nav graph
[ ] TransferViewModel and AddBeneficiaryViewModel separate — no shared state
[ ] All validation in UseCases only

## SDUI
[ ] transfer_screen.json in assets/mock/screens/
[ ] add_beneficiary_screen.json in assets/mock/screens/
[ ] BENEFICIARY_GRID columnCount prop drives LazyVerticalGrid columns
[ ] ADD_BENEFICIARY_FORM renders all 4 fields correctly

## Build
[ ] ./gradlew :feature:transfer:compileDebugKotlin — zero errors
[ ] ./gradlew :feature:transfer:testDebugUnitTest — zero failures

## Output format
FAIL: file + line + fix | PASS: ✅ | Summary: X/Y passed
