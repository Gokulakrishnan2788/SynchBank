# Validate — Home (Dashboard) Tab
# cat with: CONTEXT.md + features/validate.md + this file

## Feature-Specific Checks

### UI Pixel Accuracy (open Home_DashBoard.png and verify)
[ ] Net worth hero: 32sp bold navy, eye icon inline right
[ ] Transfer button: NavyPrimary bg, white text, left-arrow icon, 12dp radius
[ ] Pay Bills button: TealLight bg, dark teal text, 12dp radius — same height as Transfer
[ ] Relationships section: two account cards with grey placeholder icon right side
[ ] Portfolio chart: smooth teal curve, no axes, period dropdown chip top right
[ ] Activity rows: category icon in rounded square bg, amount right-aligned
[ ] Exclusive offer: dark navy card, teal badge label, white text, illustration right
[ ] Bottom nav: HOME pill dark navy selected, others grey transparent

### Behaviour
[ ] Transfer button → auto-selects PAYMENTS tab (not a new screen push)
[ ] Pay Bills → Toast "Not implemented yet"
[ ] Manage Accounts → navigates to ACCOUNTS tab
[ ] Eye icon → toggles net worth display ←→ "••••••"
[ ] Chart period dropdown → reloads chart data
[ ] Learn more → Toast "Not implemented yet"
[ ] No action crashes app — all unimplemented show Toast

### Architecture
[ ] HomeScreen uses SDUIRenderer only — zero hardcoded layout
[ ] MainScreen owns BottomNavigation + nested NavHost
[ ] Tab config loaded from bottom_nav_config.json — not hardcoded
[ ] DashboardViewModel extends BaseViewModel
[ ] No feature cross-imports

### SDUI
[ ] home_screen.json in assets/mock/screens/
[ ] bottom_nav_config.json in assets/mock/nav/
[ ] All action IDs in JSON match ViewModel handlers
[ ] All component types registered in ComponentRegistry

### Build
[ ] ./gradlew :feature:dashboard:compileDebugKotlin — zero errors
[ ] ./gradlew :feature:dashboard:testDebugUnitTest — zero failures

## Output format
FAIL: file + line + fix | PASS: ✅ | Summary: X/Y passed
