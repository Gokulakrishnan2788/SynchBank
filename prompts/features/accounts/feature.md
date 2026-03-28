# Feature: Accounts Tab
# Screenshot: app/src/main/assets/screenshots/Accounts.png
# Claude Code MUST open Accounts.png before generating any UI code.

## Screen Reference
Route: "accounts" inside MainScreen NavHost

## Pixel-Perfect UI Description

### Header
- ARCH_HEADER: showSearch=false, showNotification=true, showBack=false

### Hero
- BALANCE_HERO: "TOTAL COMBINED BALANCE" / "$1,248,392.42" 32sp bold / "+2.4% vs last month" green badge

### Account Card 1 — Elite Checking (LIGHT)
- ACCOUNT_DETAIL_CARD style=LIGHT
- White card 12dp radius, padding 16dp
- Top right: "•••• 8821" grey 12sp
- Icon: dark navy rounded square, bank/building icon white
- "Elite Checking" 18sp bold #1A1A1A
- "Primary operating account" 13sp grey
- "CURRENT BALANCE" 11sp uppercase grey → "$42,905.12" 20sp bold
- "AVAILABLE BALANCE" 11sp uppercase grey → "$41,500.00" 16sp TealAccent
- "Manage →" full width teal outlined button (border TealAccent, text TealAccent, bg TealLight, 10dp radius)

### Account Card 2 — High-Yield Savings (LIGHT)
- ACCOUNT_DETAIL_CARD style=LIGHT
- Top right: "•••• 4409"
- Icon: TealAccent rounded square, piggy bank icon white
- "High-Yield Savings" 18sp bold — with "6.85% APY" teal badge pill right
- "Wealth Reserve Fund" 13sp grey
- "TOTAL BALANCE" → "$285,487.30" 20sp bold
- "INTEREST EARNED YTD" → "+$9,214.50" GreenSuccess
- "Manage →" teal outlined button

### Account Card 3 — Wealth Management (DARK)
- ACCOUNT_DETAIL_CARD style=DARK — NavyPrimary bg full card
- Top right: "•••• 0012" white 12sp
- Icon: white/light rounded square, wallet icon navy
- "Wealth Management" 18sp bold white
- "Managed Assets Portfolio" light grey subtitle
- "TOTAL PORTFOLIO VALUE" light label → "$920,000.00" 20sp bold white
- Two columns: "EQUITY 72% $692.4k" | "CASH 28% $257.6k" — white text
- "Manage Portfolio →" white outlined button (white border, white text, transparent bg)

### Recent Activity
- SECTION_HEADER_ROW: "Recent Activity" bold + subtitle "Real-time updates across all assets" grey + "View Statement" BlueLink right
- ACTIVITY_ROW list items (same as home):
  - Apple Store Infinite Loop / ELITE CHECKING • PURCHASE • TODAY / -$1,299.00
  - Dividend Payment - NVDA / WEALTH MANAGEMENT • INCOME • YESTERDAY / +$420.50 green
  - The Modern Omakase / ELITE CHECKING • DINING • OCT 12 / -$650.00
  - Bloomberg Professional / ELITE CHECKING • SERVICE • OCT 10 / -$2,000.00

## Color Tokens
Same as composite_components.md — NavyPrimary, TealAccent, TealLight, GreenSuccess, BlueLink

## Module: :feature:accounts
## Files to generate (all layers):
API:     AccountsApiService, AccountDetailDto, assets/mock/api/accounts_list.json, assets/mock/screens/accounts_screen.json
Domain:  GetAccountsUseCase, GetAccountDetailUseCase, GetAccountTransactionsUseCase, AccountsRepository (interface+impl), AccountsMapper, AccountDetail (model)
UI:      AccountsScreen, AccountsViewModel, AccountsState/Intent/Effect, AccountsModule, assets/mock/screens/accounts_screen.json
Tests:   AccountsViewModelTest, GetAccountsUseCaseTest, AccountsScreenJsonParserTest
Validate: pixel checks per above

## MVI Contract

### AccountsState
data class AccountsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val screenModel: ScreenModel? = null,
    val totalBalance: String = "",
    val balanceChange: String = "",
    val balanceChangePositive: Boolean = true,
    val accounts: List<AccountDetail> = emptyList(),
    val recentActivity: List<ActivityItem> = emptyList()
) : UiState

### AccountsIntent
sealed class AccountsIntent : UiIntent {
    object LoadScreen : AccountsIntent()
    object RefreshAccounts : AccountsIntent()
    data class ManageAccount(val accountId: String) : AccountsIntent()
    object ViewStatement : AccountsIntent()
    data class HandleAction(val actionId: String) : AccountsIntent()
    object ClearError : AccountsIntent()
}

### AccountsEffect
sealed class AccountsEffect : UiEffect {
    data class NavigateToAccountDetail(val accountId: String) : AccountsEffect()
    data class ShowToast(val message: String) : AccountsEffect()
}

### Reducer
LoadScreen → GetAccountsUseCase → setState(accounts, totalBalance, recentActivity, screenModel)
ManageAccount(id) → setEffect(ShowToast("Not implemented yet"))
ViewStatement → setEffect(ShowToast("Not implemented yet"))
HandleAction("MANAGE_*") → ShowToast("Not implemented yet")

## SDUI JSON — accounts_screen.json
{
  "screenId": "accounts",
  "version": "1.0",
  "metadata": { "title": "Accounts", "analyticsTag": "accounts_screen" },
  "layout": { "type": "LAZY_COLUMN", "padding": { "horizontal": 0, "vertical": 0 } },
  "components": [
    { "id": "header", "type": "ARCH_HEADER", "props": { "logoAsset": "ic_architect_avatar", "title": "Architect", "showSearch": false, "showNotification": true, "showBack": false, "showAvatar": false } },
    { "id": "spacer_1", "type": "SPACER", "props": { "height": 16 } },
    { "id": "balance_hero", "type": "BALANCE_HERO", "props": { "label": "TOTAL COMBINED BALANCE", "amount": "$1,248,392.42", "changeBadge": "+2.4% vs last month", "badgePositive": true } },
    { "id": "spacer_2", "type": "SPACER", "props": { "height": 20 } },
    {
      "id": "card_checking",
      "type": "ACCOUNT_DETAIL_CARD",
      "props": {
        "cardTag": "•••• 8821", "iconAsset": "ic_account_bank", "iconBgColor": "IconBgNavy",
        "accountName": "Elite Checking", "accountSubtitle": "Primary operating account",
        "primaryLabel": "CURRENT BALANCE", "primaryAmount": "$42,905.12",
        "secondaryLabel": "AVAILABLE BALANCE", "secondaryAmount": "$41,500.00", "secondaryAmountColor": "TealAccent",
        "manageLabel": "Manage →", "style": "LIGHT", "manageAction": "MANAGE_ACC_001"
      },
      "action": "MANAGE_ACC_001"
    },
    { "id": "spacer_3", "type": "SPACER", "props": { "height": 16 } },
    {
      "id": "card_savings",
      "type": "ACCOUNT_DETAIL_CARD",
      "props": {
        "cardTag": "•••• 4409", "iconAsset": "ic_account_savings", "iconBgColor": "IconBgTeal",
        "accountName": "High-Yield Savings", "accountSubtitle": "Wealth Reserve Fund",
        "badgeLabel": "6.85% APY",
        "primaryLabel": "TOTAL BALANCE", "primaryAmount": "$285,487.30",
        "secondaryLabel": "INTEREST EARNED YTD", "secondaryAmount": "+$9,214.50", "secondaryAmountColor": "GreenSuccess",
        "manageLabel": "Manage →", "style": "LIGHT", "manageAction": "MANAGE_ACC_002"
      },
      "action": "MANAGE_ACC_002"
    },
    { "id": "spacer_4", "type": "SPACER", "props": { "height": 16 } },
    {
      "id": "card_wealth",
      "type": "ACCOUNT_DETAIL_CARD",
      "props": {
        "cardTag": "•••• 0012", "iconAsset": "ic_account_wealth", "iconBgColor": "White",
        "accountName": "Wealth Management", "accountSubtitle": "Managed Assets Portfolio",
        "primaryLabel": "TOTAL PORTFOLIO VALUE", "primaryAmount": "$920,000.00",
        "equityPercent": "72%", "equityValue": "$692.4k",
        "cashPercent": "28%", "cashValue": "$257.6k",
        "manageLabel": "Manage Portfolio →", "style": "DARK", "manageAction": "MANAGE_ACC_003"
      },
      "action": "MANAGE_ACC_003"
    },
    { "id": "spacer_5", "type": "SPACER", "props": { "height": 28 } },
    { "id": "activity_header", "type": "SECTION_HEADER_ROW", "props": { "title": "Recent Activity", "trailingLabel": "View Statement", "trailingColor": "BlueLink", "trailingAction": "VIEW_STATEMENT" } },
    { "id": "activity_subtitle", "type": "TEXT", "props": { "text": "Real-time updates across all assets", "style": "CAPTION", "color": "TextSecondary" } },
    { "id": "spacer_6", "type": "SPACER", "props": { "height": 12 } },
    { "id": "act_1", "type": "ACTIVITY_ROW", "props": { "title": "Apple Store Infinite Loop", "subtitle": "ELITE CHECKING • PURCHASE • TODAY", "amount": "-$1,299.00", "amountPositive": false, "iconAsset": "ic_txn_shopping", "iconBgColor": "IconBgGrey" } },
    { "id": "act_2", "type": "ACTIVITY_ROW", "props": { "title": "Dividend Payment - NVDA", "subtitle": "WEALTH MANAGEMENT • INCOME • YESTERDAY", "amount": "+$420.50", "amountPositive": true, "iconAsset": "ic_txn_income", "iconBgColor": "IconBgGreen" } },
    { "id": "act_3", "type": "ACTIVITY_ROW", "props": { "title": "The Modern Omakase", "subtitle": "ELITE CHECKING • DINING • OCT 12", "amount": "-$650.00", "amountPositive": false, "iconAsset": "ic_txn_dining", "iconBgColor": "IconBgGrey" } },
    { "id": "act_4", "type": "ACTIVITY_ROW", "props": { "title": "Bloomberg Professional", "subtitle": "ELITE CHECKING • SERVICE • OCT 10", "amount": "-$2,000.00", "amountPositive": false, "iconAsset": "ic_txn_service", "iconBgColor": "IconBgGrey" } },
    { "id": "spacer_end", "type": "SPACER", "props": { "height": 80 } }
  ],
  "actions": {
    "MANAGE_ACC_001": { "type": "SHOW_DIALOG", "params": { "message": "Not implemented yet" } },
    "MANAGE_ACC_002": { "type": "SHOW_DIALOG", "params": { "message": "Not implemented yet" } },
    "MANAGE_ACC_003": { "type": "SHOW_DIALOG", "params": { "message": "Not implemented yet" } },
    "VIEW_STATEMENT":  { "type": "SHOW_DIALOG", "params": { "message": "Not implemented yet" } }
  }
}

## Mock JSON — accounts_list.json
{
  "success": true,
  "data": {
    "totalBalance": "$1,248,392.42",
    "balanceChange": "+2.4% vs last month",
    "balanceChangePositive": true,
    "accounts": [
      { "id": "acc_001", "cardTag": "•••• 8821", "iconAsset": "ic_account_bank", "iconBgColor": "IconBgNavy", "accountName": "Elite Checking", "accountSubtitle": "Primary operating account", "primaryLabel": "CURRENT BALANCE", "primaryAmount": "$42,905.12", "secondaryLabel": "AVAILABLE BALANCE", "secondaryAmount": "$41,500.00", "secondaryAmountColor": "TealAccent", "manageLabel": "Manage →", "style": "LIGHT" },
      { "id": "acc_002", "cardTag": "•••• 4409", "iconAsset": "ic_account_savings", "iconBgColor": "IconBgTeal", "accountName": "High-Yield Savings", "accountSubtitle": "Wealth Reserve Fund", "badgeLabel": "6.85% APY", "primaryLabel": "TOTAL BALANCE", "primaryAmount": "$285,487.30", "secondaryLabel": "INTEREST EARNED YTD", "secondaryAmount": "+$9,214.50", "secondaryAmountColor": "GreenSuccess", "manageLabel": "Manage →", "style": "LIGHT" },
      { "id": "acc_003", "cardTag": "•••• 0012", "iconAsset": "ic_account_wealth", "iconBgColor": "White", "accountName": "Wealth Management", "accountSubtitle": "Managed Assets Portfolio", "primaryLabel": "TOTAL PORTFOLIO VALUE", "primaryAmount": "$920,000.00", "equityPercent": "72%", "equityValue": "$692.4k", "cashPercent": "28%", "cashValue": "$257.6k", "manageLabel": "Manage Portfolio →", "style": "DARK" }
    ]
  }
}

## Feature Rules
- All card styles (LIGHT/DARK, iconBgColor, amounts) driven from server JSON
- Manage buttons → Toast "Not implemented yet" for now
- View Statement → Toast "Not implemented yet"
- ACCOUNT_DETAIL_CARD handles LIGHT vs DARK rendering based on style prop
- Equity/Cash columns only shown when equityPercent is non-null (DARK card only)
- Screen 100% SDUI
