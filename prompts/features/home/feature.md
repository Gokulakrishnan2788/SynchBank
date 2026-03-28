# Feature: Home Tab (Dashboard)
# Master overview — load alongside specific layer files
# Screenshot: app/src/main/assets/screenshots/Home_DashBoard.png
# Claude Code MUST open Home_DashBoard.png before generating any UI code.

## Screen Reference
File: Home_DashBoard.png
Tab: HOME (default selected after login)
Route: "home" inside MainScreen NavHost

## Pixel-Perfect UI Description

### Header
- ARCH_HEADER component
- showSearch: true, showNotification: true, showBack: false
- Right icons: magnifier + bell, both outlined grey

### Section 1 — Total Net Worth
- NET_WORTH_HEADER component
- Label: "TOTAL NET WORTH" — 11sp uppercase grey #6B7280
- Amount: "$142,850.42" — 32sp bold #1B2A5E
- Eye icon inline right of amount — toggles visibility
- When hidden: amount shows "••••••"
- Below: "+2.4% vs last month" — green #22C55E badge, tiny arrow up, 12sp

### Section 2 — Action Buttons
- ACTION_BUTTON_ROW component
- Two equal-width buttons, 12dp corner radius, 8dp gap between
- Button 1 "Transfer": NavyPrimary bg, white text, left-arrow icon
  → clicking Transfer: auto-selects Payments tab (NavigationEffect)
- Button 2 "Pay Bills": TealLight bg #E0F7F4, dark teal text, receipt icon

### Section 3 — Relationships
- SECTION_HEADER_ROW: "Relationships" + "Manage Accounts" BlueLink right
- Two RELATIONSHIP_ACCOUNT_CARD items stacked:
  - Card 1: ELITE CHECKING / $42,301.15 / Available • ••• 9012
  - Card 2: HIGH-YIELD SAVINGS / $100,549.27 / 4.50% APY • ••• 4481
  - White card, subtle border #F0F1F3, 12dp radius
  - Grey placeholder icon square right side

### Section 4 — Portfolio Growth
- PORTFOLIO_CHART_CARD component
- Title: "Portfolio Growth" 20sp semibold
- Subtitle: "Insights from your last 6 months" grey 14sp
- Dropdown chip right: "Last 6 Months ↓" — outlined grey chip
- Smooth teal line chart, minimal, no axes, fills card width
- White card, 12dp radius, subtle shadow

### Section 5 — Activity
- SECTION_HEADER_ROW: "Activity" + "View All" BlueLink right
- Lazy list — 4 ACTIVITY_ROW items:
  - Apple Store / Electronics • Today / -$1,299.00 / grey icon bg
  - Salary Deposit / Income • Oct 15 / +$8,450.00 green / green icon bg
  - The Oak Bistro / Dining • Oct 14 / -$84.20 / grey icon bg
  - Tesla Supercharger / Transport • Oct 12 / -$22.50 / grey icon bg

### Section 6 — Exclusive Offer
- EXCLUSIVE_OFFER_BANNER component
- Dark navy bg #1B2A5E, full width, 12dp radius
- "EXCLUSIVE OFFER" teal uppercase small label
- "Unlock Private Wealth Advisory Services" white bold
- "Learn more →" white small text link
- Right: abstract blue/purple illustration (decorative asset)

### Bottom Navigation
- HOME tab selected — dark navy pill bg, white icon+text
- Other tabs: transparent bg, grey

## Color Tokens
NavyPrimary:    #1B2A5E
TealAccent:     #00C8A0
TealLight:      #E0F7F4
BackgroundGrey: #F5F6F8
GreenSuccess:   #22C55E
BlueLink:       #2563EB
TextPrimary:    #1A1A1A
TextSecondary:  #6B7280

## Module: :feature:dashboard
## Files to generate:
API:     DashboardApiService, DashboardDto, assets/mock/api/dashboard.json
Domain:  GetDashboardUseCase, GetActivityUseCase, DashboardRepository (interface+impl), DashboardMapper, DashboardData (model)
UI:      HomeScreen, DashboardViewModel, DashboardState/Intent/Effect, DashboardModule, assets/mock/screens/home_screen.json
Tests:   DashboardViewModelTest, GetDashboardUseCaseTest, HomeScreenJsonParserTest
Also:    MainScreen (bottom nav host), MainViewModel, MainState/Intent/Effect, BottomNavConfig

## Feature Rules
- Default tab on login success → HOME (NavigateToMain effect from LoginViewModel)
- Transfer button → dispatches NavigateToPayments effect → MainViewModel.SelectTab(PAYMENTS)
- Pay Bills → Toast "Not implemented yet"
- Manage Accounts link → dispatches NavigateToAccounts effect
- View All activity → Toast "Not implemented yet" (or navigate to transactions if exists)
- Net worth eye toggle → local state only (no API call)
- Chart period dropdown → dispatches LoadChart(period) intent
- Learn more → Toast "Not implemented yet"
- All card themes driven from server JSON
- Screen fully SDUI — zero hardcoded Composable layout in HomeScreen.kt
