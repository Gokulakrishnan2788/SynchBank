# Validate — Accounts Tab
# cat with: CONTEXT.md + features/validate.md + features/accounts/feature.md + this file

## UI Pixel Accuracy (open Accounts.png and verify)
[ ] BALANCE_HERO: "TOTAL COMBINED BALANCE" label, large bold amount, green change badge
[ ] Elite Checking card: white, navy icon square, current+available balance labels, teal Manage button
[ ] High-Yield Savings card: teal icon square, "6.85% APY" badge pill, green interest YTD
[ ] Wealth Management card: DARK navy bg, white text throughout, equity/cash two columns
[ ] Manage Portfolio button: white outlined (white border, white text, transparent bg)
[ ] Recent Activity rows: icon in rounded square bg, amount right-aligned
[ ] Bottom nav: ACCOUNTS pill dark navy selected

## Behaviour
[ ] All Manage buttons → Toast "Not implemented yet"
[ ] View Statement → Toast "Not implemented yet"
[ ] ACCOUNT_DETAIL_CARD DARK style: all text white, button white outlined
[ ] ACCOUNT_DETAIL_CARD LIGHT style: dark text, teal outlined Manage button
[ ] Equity/Cash columns only shown on Wealth Management card (when equityPercent non-null)

## Architecture
[ ] AccountsScreen uses SDUIRenderer only — zero hardcoded layout
[ ] AccountsViewModel extends BaseViewModel
[ ] No feature cross-imports

## Build
[ ] ./gradlew :feature:accounts:compileDebugKotlin — zero errors
[ ] ./gradlew :feature:accounts:testDebugUnitTest — zero failures

## Output format
FAIL: file + line + fix | PASS: ✅ | Summary: X/Y passed
