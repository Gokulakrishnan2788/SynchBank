# Home (Dashboard) — UI Layer
# cat with: CONTEXT.md + contract/mvi.md + contract/sdui_contract.md
#           + contract/renderer.md + composite/composite_components.md + this file
# Screenshot: app/src/main/assets/screenshots/Home_DashBoard.png
# Claude Code MUST open and analyse Home_DashBoard.png before writing any code.

## Generate these files ONLY
- HomeScreen.kt
- DashboardViewModel.kt
- DashboardState.kt
- MainScreen.kt
- MainViewModel.kt
- MainState.kt
- DashboardModule.kt
- assets/mock/screens/home_screen.json
- assets/mock/nav/bottom_nav_config.json

## HomeScreen.kt
- Route: "home" inside MainScreen NavHost
- Uses SDUIRenderer ONLY — zero hardcoded layout
- init → DashboardIntent.LoadScreen
- Transfer action effect → MainViewModel.handleIntent(MainIntent.SelectTab(PAYMENTS))
- ShowToast effect → show Android Toast
- Has @Preview

## MainScreen.kt
- Scaffold with BottomNavigation
- Tabs driven from MainState.tabs (loaded from bottom_nav_config.json)
- Selected tab: dark navy rounded pill bg, white icon+label
- Unselected: transparent bg, grey icon+label (#9CA3AF)
- Tab bar background: white, 1dp top divider #E5E7EB
- Nested NavHost for: home / payments / accounts / profile
- Tab icon size: 24dp, label 10sp

## SDUI JSON — home_screen.json
{
  "screenId": "home",
  "version": "1.0",
  "metadata": { "title": "Home", "analyticsTag": "home_screen" },
  "layout": { "type": "LAZY_COLUMN", "padding": { "horizontal": 0, "vertical": 0 } },
  "components": [
    {
      "id": "header",
      "type": "ARCH_HEADER",
      "props": {
        "logoAsset": "ic_architect_avatar",
        "title": "Architect",
        "showSearch": true,
        "showNotification": true,
        "showBack": false,
        "showAvatar": false
      }
    },
    { "id": "spacer_1", "type": "SPACER", "props": { "height": 16 } },
    {
      "id": "net_worth",
      "type": "NET_WORTH_HEADER",
      "props": {
        "label": "TOTAL NET WORTH",
        "amount": "$142,850.42",
        "changeText": "+2.4% vs last month",
        "changePositive": true,
        "amountVisible": true
      },
      "action": "TOGGLE_NET_WORTH"
    },
    { "id": "spacer_2", "type": "SPACER", "props": { "height": 20 } },
    {
      "id": "action_buttons",
      "type": "ACTION_BUTTON_ROW",
      "props": {
        "buttons": [
          {
            "id": "btn_transfer",
            "label": "Transfer",
            "icon": "ic_transfer_arrow",
            "bgColor": "NavyPrimary",
            "textColor": "White",
            "action": "TRANSFER"
          },
          {
            "id": "btn_pay_bills",
            "label": "Pay Bills",
            "icon": "ic_receipt",
            "bgColor": "TealLight",
            "textColor": "TealDark",
            "action": "PAY_BILLS"
          }
        ]
      }
    },
    { "id": "spacer_3", "type": "SPACER", "props": { "height": 28 } },
    {
      "id": "relationships_header",
      "type": "SECTION_HEADER_ROW",
      "props": {
        "title": "Relationships",
        "trailingLabel": "Manage Accounts",
        "trailingColor": "BlueLink",
        "trailingAction": "MANAGE_ACCOUNTS"
      }
    },
    { "id": "spacer_4", "type": "SPACER", "props": { "height": 12 } },
    {
      "id": "account_checking",
      "type": "RELATIONSHIP_ACCOUNT_CARD",
      "props": {
        "accountType": "ELITE CHECKING",
        "balance": "$42,301.15",
        "detail": "Available • ••• 9012",
        "iconAsset": "ic_account_checking"
      },
      "action": "ACCOUNT_CHECKING_DETAIL"
    },
    { "id": "spacer_5", "type": "SPACER", "props": { "height": 8 } },
    {
      "id": "account_savings",
      "type": "RELATIONSHIP_ACCOUNT_CARD",
      "props": {
        "accountType": "HIGH-YIELD SAVINGS",
        "balance": "$100,549.27",
        "detail": "4.50% APY • ••• 4481",
        "iconAsset": "ic_account_savings"
      },
      "action": "ACCOUNT_SAVINGS_DETAIL"
    },
    { "id": "spacer_6", "type": "SPACER", "props": { "height": 28 } },
    {
      "id": "portfolio_chart",
      "type": "PORTFOLIO_CHART_CARD",
      "props": {
        "title": "Portfolio Growth",
        "subtitle": "Insights from your last 6 months",
        "selectedPeriod": "Last 6 Months",
        "periods": ["Last 3 Months", "Last 6 Months", "Last Year"],
        "dataPoints": [85000, 92000, 88000, 105000, 118000, 142850],
        "chartColor": "TealAccent"
      },
      "action": "CHANGE_PERIOD"
    },
    { "id": "spacer_7", "type": "SPACER", "props": { "height": 28 } },
    {
      "id": "activity_header",
      "type": "SECTION_HEADER_ROW",
      "props": {
        "title": "Activity",
        "trailingLabel": "View All",
        "trailingColor": "BlueLink",
        "trailingAction": "VIEW_ALL_ACTIVITY"
      }
    },
    { "id": "spacer_8", "type": "SPACER", "props": { "height": 12 } },
    {
      "id": "activity_1",
      "type": "ACTIVITY_ROW",
      "props": { "title": "Apple Store", "subtitle": "Electronics • Today", "amount": "-$1,299.00", "amountPositive": false, "iconAsset": "ic_txn_shopping", "iconBgColor": "IconBgGrey" }
    },
    {
      "id": "activity_2",
      "type": "ACTIVITY_ROW",
      "props": { "title": "Salary Deposit", "subtitle": "Income • Oct 15", "amount": "+$8,450.00", "amountPositive": true, "iconAsset": "ic_txn_income", "iconBgColor": "IconBgGreen" }
    },
    {
      "id": "activity_3",
      "type": "ACTIVITY_ROW",
      "props": { "title": "The Oak Bistro", "subtitle": "Dining • Oct 14", "amount": "-$84.20", "amountPositive": false, "iconAsset": "ic_txn_dining", "iconBgColor": "IconBgGrey" }
    },
    {
      "id": "activity_4",
      "type": "ACTIVITY_ROW",
      "props": { "title": "Tesla Supercharger", "subtitle": "Transport • Oct 12", "amount": "-$22.50", "amountPositive": false, "iconAsset": "ic_txn_transport", "iconBgColor": "IconBgGrey" }
    },
    { "id": "spacer_9", "type": "SPACER", "props": { "height": 28 } },
    {
      "id": "exclusive_offer",
      "type": "EXCLUSIVE_OFFER_BANNER",
      "props": {
        "badgeLabel": "EXCLUSIVE OFFER",
        "title": "Unlock Private Wealth Advisory Services",
        "ctaLabel": "Learn more →",
        "bgColor": "NavyPrimary",
        "illustrationAsset": "ic_offer_illustration",
        "ctaAction": "LEARN_MORE"
      }
    },
    { "id": "spacer_end", "type": "SPACER", "props": { "height": 80 } }
  ],
  "actions": {
    "TOGGLE_NET_WORTH":      { "type": "API_CALL", "endpoint": "/dashboard/toggle_worth", "method": "POST" },
    "TRANSFER":              { "type": "NAVIGATE", "destination": "payments" },
    "PAY_BILLS":             { "type": "SHOW_DIALOG", "params": { "message": "Not implemented yet" } },
    "MANAGE_ACCOUNTS":       { "type": "NAVIGATE", "destination": "accounts" },
    "ACCOUNT_CHECKING_DETAIL": { "type": "NAVIGATE", "destination": "accounts" },
    "ACCOUNT_SAVINGS_DETAIL":  { "type": "NAVIGATE", "destination": "accounts" },
    "CHANGE_PERIOD":         { "type": "API_CALL", "endpoint": "/dashboard/chart", "method": "GET" },
    "VIEW_ALL_ACTIVITY":     { "type": "SHOW_DIALOG", "params": { "message": "Not implemented yet" } },
    "LEARN_MORE":            { "type": "SHOW_DIALOG", "params": { "message": "Not implemented yet" } }
  }
}

## bottom_nav_config.json
{
  "tabs": [
    { "id": "home",     "label": "HOME",     "icon": "ic_nav_home",     "route": "home" },
    { "id": "payments", "label": "PAYMENTS", "icon": "ic_nav_payments", "route": "payments" },
    { "id": "accounts", "label": "ACCOUNTS", "icon": "ic_nav_accounts", "route": "accounts" },
    { "id": "profile",  "label": "PROFILE",  "icon": "ic_nav_profile",  "route": "profile" }
  ]
}

## Design Tokens (match screenshot exactly)
Background:        #F5F6F8
Header bg:         White
Net worth amount:  #1B2A5E  32sp Bold
Change badge bg:   #DCFCE7  text: #22C55E
Transfer btn:      #1B2A5E bg, white text
Pay Bills btn:     #E0F7F4 bg, #008F72 text
Card bg:           White, border #F0F1F3, radius 12dp
Activity positive: #22C55E
Activity negative: #1A1A1A
Section title:     20sp SemiBold #1A1A1A
Trailing link:     #2563EB 14sp
Nav selected pill: #1B2A5E, white text+icon
Nav unselected:    transparent, #9CA3AF
