# Composite & Shared SDUI Components
# cat with: CONTEXT.md + contract/sdui_contract.md + contract/renderer.md + this file
# Run ONCE (Phase 3) before any feature screen generation.
# Screenshot references: all 5 screens share these components.

## ── SCREENSHOT REFERENCE ──────────────────────────────────────────────────
# Place screenshots in: app/src/main/assets/screenshots/
# - Home_DashBoard.png
# - Payments.png
# - Accounts.png
# - Add_Beneficiary.png
# - _User_Profile.png
# Claude Code MUST open and read these images before generating any UI code.

## ── DESIGN TOKENS ─────────────────────────────────────────────────────────
# All values extracted from screenshots — add to DesignTokens.kt in :core:ui

### Colors
NavyPrimary:       #1B2A5E   → primary buttons, dark cards, selected nav tab bg
NavyLight:         #2A3F7E   → secondary navy tones
TealAccent:        #00C8A0   → teal highlights, badges, available balance, manage button
TealLight:         #E0F7F4   → Pay Bills button bg, teal soft backgrounds
TealDark:          #008F72   → teal icon backgrounds
BackgroundGrey:    #F5F6F8   → all screen backgrounds
White:             #FFFFFF   → cards, input fields
TextPrimary:       #1A1A1A   → main headings, balances
TextSecondary:     #6B7280   → subtitles, labels, section headers
TextTertiary:      #9CA3AF   → captions, account numbers, hints
GreenSuccess:      #22C55E   → positive amounts, income transactions
RedDestructive:    #EF4444   → logout button border+text, negative indicator
BlueLink:          #2563EB   → "Manage Accounts", "View All", "View Statement" links
DividerColor:      #E5E7EB   → card dividers
CardBorder:        #F0F1F3   → subtle card borders
DashedBorder:      #C5C9D0   → new recipient tile dashed border
IconBgNavy:        #1B2A5E   → icon container bg (dark cards)
IconBgTeal:        #00C8A0   → savings icon bg
IconBgGrey:        #F3F4F6   → activity icon bg (neutral transactions)
IconBgGreen:       #DCFCE7   → activity icon bg (income)

### Typography
DisplayLarge:  32sp Bold   → net worth amount, account balances hero
DisplayMedium: 28sp Bold   → Add Beneficiary page title
HeadingLarge:  24sp Bold   → Transfer title, Alexander Sterling name
HeadingMedium: 20sp SemiBold → section titles (Relationships, Activity, Portfolio Growth)
BodyLarge:     16sp Medium → account names, row labels
BodyMedium:    14sp Regular → subtitles, descriptions
LabelSmall:    11sp Medium Uppercase → TOTAL NET WORTH, SELECT SOURCE ACCOUNT, SECURITY & IDENTITY
Caption:       12sp Regular → member since, footer, account numbers

### Spacing
XS:  4dp
SM:  8dp
MD:  16dp
LG:  24dp
XL:  32dp

### Shape
CardRadius:       12dp   → all white cards
ButtonRadius:     12dp   → primary/secondary buttons
ChipRadius:       20dp   → amount chips, badge pills
AvatarRadius:     12dp   → avatar image corners
IconBgRadius:     10dp   → icon container squares
NavPillRadius:    8dp    → selected bottom nav tab pill

## ── NEW SDUI COMPONENT TYPES ──────────────────────────────────────────────
# Append ALL of these to SduiComponentType enum and ComponentRegistry
# DO NOT modify existing registrations

### ARCH_HEADER
# Shared top bar across ALL screens. Icon slots configurable from JSON.
Props:
{
  "logoAsset": "ic_architect_avatar",
  "title": "Architect",
  "showSearch": Boolean,        // Home: true, others: false
  "showNotification": Boolean,  // always true
  "showBack": Boolean,          // Add Beneficiary: true
  "showAvatar": Boolean,        // Add Beneficiary: true
  "backAction": String?         // ACTION_ID for back
}
Composable: ArchHeaderComponent.kt in :core:ui

### NET_WORTH_HEADER
# Home screen hero — total net worth with eye toggle
Props:
{
  "label": "TOTAL NET WORTH",
  "amount": "$142,850.42",
  "changeText": "+2.4% vs last month",
  "changePositive": Boolean,
  "amountVisible": Boolean      // eye toggle state
}
action: "TOGGLE_NET_WORTH_VISIBILITY"
Composable: NetWorthHeaderComponent.kt in :core:ui

### ACTION_BUTTON_ROW
# Home screen — Transfer + Pay Bills row
Props:
{
  "buttons": [
    {
      "id": String,
      "label": String,
      "icon": String,           // drawable token
      "bgColor": String,        // TOKEN e.g. "NavyPrimary" or "TealLight"
      "textColor": String,      // TOKEN
      "action": String          // ACTION_ID
    }
  ]
}
Composable: ActionButtonRowComponent.kt in :core:ui

### RELATIONSHIP_ACCOUNT_CARD
# Home screen account list item (compact card)
Props:
{
  "accountType": String,        // "ELITE CHECKING" uppercase
  "balance": String,            // "$42,301.15"
  "detail": String,             // "Available • ••• 9012"
  "iconAsset": String,          // placeholder icon
  "action": String?
}
Composable: RelationshipAccountCard.kt in :core:ui

### PORTFOLIO_CHART_CARD
# Home screen — line chart with dropdown period selector
Props:
{
  "title": "Portfolio Growth",
  "subtitle": "Insights from your last 6 months",
  "selectedPeriod": String,     // "Last 6 Months"
  "periods": [String],          // ["Last 3 Months","Last 6 Months","Last Year"]
  "dataPoints": [Double],       // chart values
  "chartColor": "TealAccent"
}
action: "CHANGE_PERIOD"
Composable: PortfolioChartCard.kt in :core:ui
# Use MPAndroidChart or Canvas-based line chart — smooth curve, no axes labels

### ACTIVITY_ROW
# Shared — Home activity list + Accounts recent activity
Props:
{
  "title": String,              // "Apple Store"
  "subtitle": String,           // "Electronics • Today"
  "amount": String,             // "-$1,299.00"
  "amountPositive": Boolean,
  "iconAsset": String,          // drawable token
  "iconBgColor": String         // TOKEN e.g. "IconBgGrey" or "IconBgGreen"
}
Composable: ActivityRowComponent.kt in :core:ui

### EXCLUSIVE_OFFER_BANNER
# Home screen bottom banner — dark navy with illustration
Props:
{
  "badgeLabel": "EXCLUSIVE OFFER",
  "title": String,
  "ctaLabel": "Learn more →",
  "bgColor": "NavyPrimary",
  "illustrationAsset": String,
  "ctaAction": String
}
Composable: ExclusiveOfferBanner.kt in :core:ui

### SECTION_HEADER_ROW
# Reused across Home, Accounts, Payments
Props:
{
  "title": String,
  "trailingLabel": String?,     // "Manage Accounts", "View All", "View Statement"
  "trailingColor": String?,     // "BlueLink" or "TealAccent"
  "trailingAction": String?
}
Composable: SectionHeaderRow.kt in :core:ui

### ACCOUNT_DETAIL_CARD
# Accounts screen — full account card (light + dark variants)
Props:
{
  "cardTag": String,            // "•••• 8821"
  "iconAsset": String,
  "iconBgColor": String,        // "IconBgNavy" or "IconBgTeal"
  "accountName": String,
  "accountSubtitle": String,
  "badgeLabel": String?,        // "6.85% APY" — null if not applicable
  "primaryLabel": String,       // "CURRENT BALANCE"
  "primaryAmount": String,
  "secondaryLabel": String?,    // "AVAILABLE BALANCE" or "INTEREST EARNED YTD"
  "secondaryAmount": String?,
  "secondaryAmountColor": String?, // "TealAccent" or "GreenSuccess"
  "equityPercent": String?,     // "72%" — Wealth card only
  "equityValue": String?,       // "$692.4k"
  "cashPercent": String?,       // "28%"
  "cashValue": String?,         // "$257.6k"
  "manageLabel": String,        // "Manage →" or "Manage Portfolio →"
  "style": "LIGHT" | "DARK",   // LIGHT=white, DARK=navy
  "manageAction": String
}
Composable: AccountDetailCard.kt in :core:ui

### BALANCE_HERO
# Accounts screen top hero
Props:
{
  "label": "TOTAL COMBINED BALANCE",
  "amount": "$1,248,392.42",
  "changeBadge": "+2.4% vs last month",
  "badgePositive": Boolean
}
Composable: BalanceHeroComponent.kt in :core:ui

### SOURCE_ACCOUNT_SELECTOR
# Payments screen — dropdown card for source account
Props:
{
  "selectedAccountName": String,  // "Wealth Management — •••• 8829"
  "availableBalance": String,     // "$142,850.00"
  "balanceColor": "TealAccent",
  "accounts": [
    { "id": String, "name": String, "maskedNumber": String, "balance": String }
  ]
}
action: "ACCOUNT_SELECTED"
Composable: SourceAccountSelector.kt in :core:ui

### AMOUNT_INPUT_CARD
# Payments screen — large $ input with quick-add chips
Props:
{
  "currencySymbol": "$",
  "hint": "0.00",
  "quickAmounts": [
    { "label": "+$1,000", "value": 1000 },
    { "label": "+$5,000", "value": 5000 },
    { "label": "+$10,000", "value": 10000 }
  ]
}
action: "AMOUNT_CHANGED"
Composable: AmountInputCard.kt in :core:ui
# Large ghost "0.00" text (~40sp), $ symbol left, chips below with outlined rounded border

### BENEFICIARY_GRID
# Payments screen — dynamic grid of beneficiary tiles
Props:
{
  "columnCount": Int,           // 1, 2, or 3 — from server
  "beneficiaries": [
    {
      "id": String,
      "name": String,
      "subtitle": String,
      "avatarUrl": String?,
      "avatarAsset": String?,
      "tileBgColor": String?,   // server-configured per tile
      "tileImageUrl": String?,  // server-configured bg image per tile
      "isSelected": Boolean
    }
  ],
  "showAddNew": Boolean         // always true — adds NEW RECIPIENT last
}
action: "BENEFICIARY_SELECTED" | "ADD_BENEFICIARY"
Composable: BeneficiaryGridComponent.kt in :core:ui
# Selected tile: NavyPrimary bg, white text, teal checkmark on avatar
# New tile: dashed border, + icon center, "NEW RECIPIENT" label

### TRANSFER_LIMIT_BANNER
# Payments screen — teal info banner
Props:
{
  "label": "Transfer Limit",
  "amount": "$250,000",
  "suffix": "DAILY REMAINING",
  "bgColor": "TealAccent",
  "illustrationAsset": "ic_bank_building"
}
Composable: TransferLimitBanner.kt in :core:ui

### PROFILE_INFO_CARD
# Profile screen — labelled field display card
Props:
{
  "fields": [
    { "label": String, "value": String }
  ]
}
Composable: ProfileInfoCard.kt in :core:ui
# Divider between each field inside the card

### PROFILE_SETTINGS_CARD
# Profile screen — settings rows card
Props:
{
  "rows": [
    {
      "iconAsset": String,
      "iconBgColor": String,    // subtle teal tint square
      "label": String,
      "sublabel": String?,
      "trailingType": "ARROW" | "TOGGLE" | "LABEL",
      "trailingValue": String?,
      "toggleEnabled": Boolean?,
      "action": String?
    }
  ]
}
Composable: ProfileSettingsCard.kt in :core:ui
# Divider between each row

### AVATAR_SECTION
# Profile screen — avatar + name + member since
Props:
{
  "avatarUrl": String?,
  "avatarAsset": String?,
  "name": String,
  "memberSince": String,
  "editAction": String
}
Composable: AvatarSectionComponent.kt in :core:ui
# Square avatar ~80dp, corner radius 12dp, edit pencil badge bottom-right

### ADD_BENEFICIARY_FORM
# Add Beneficiary sub-screen form
Props:
{
  "fields": [
    {
      "id": String,
      "label": String,
      "hint": String,
      "helperText": String?,
      "inputType": "TEXT" | "NUMBER" | "DROPDOWN",
      "options": [String]?      // for DROPDOWN
    }
  ]
}
Composable: AddBeneficiaryForm.kt in :core:ui

### VERIFICATION_CARD
# Add Beneficiary — secure verification info card
Props:
{
  "iconAsset": "ic_shield_check",
  "iconBgColor": "TealLight",
  "title": "Secure Verification",
  "description": String,
  "illustrationAsset": String?
}
Composable: VerificationCard.kt in :core:ui

## ── BOTTOM NAVIGATION ────────────────────────────────────────────────────
# MainScreen.kt hosts Scaffold + BottomNav + nested NavHost
# Tab config is DYNAMIC from JSON — icon and label from server

### BottomNavConfig JSON (assets/mock/nav/bottom_nav_config.json)
{
  "tabs": [
    { "id": "home",     "label": "HOME",     "icon": "ic_nav_home",     "route": "home" },
    { "id": "payments", "label": "PAYMENTS", "icon": "ic_nav_payments", "route": "payments" },
    { "id": "accounts", "label": "ACCOUNTS", "icon": "ic_nav_accounts", "route": "accounts" },
    { "id": "profile",  "label": "PROFILE",  "icon": "ic_nav_profile",  "route": "profile" }
  ]
}

### Selected tab visual (from screenshot):
# Selected: dark navy rounded pill bg behind icon+label, white icon+text
# Unselected: transparent bg, grey icon+text
# Tab bar bg: white, top divider subtle grey line

## ── COMPONENT REGISTRY ADDITIONS ────────────────────────────────────────
# Append to ComponentRegistry.kt in :engine:sdui — DO NOT replace existing

SduiComponentType.ARCH_HEADER              -> ArchHeaderComponent
SduiComponentType.NET_WORTH_HEADER         -> NetWorthHeaderComponent
SduiComponentType.ACTION_BUTTON_ROW        -> ActionButtonRowComponent
SduiComponentType.RELATIONSHIP_ACCOUNT_CARD -> RelationshipAccountCard
SduiComponentType.PORTFOLIO_CHART_CARD     -> PortfolioChartCard
SduiComponentType.ACTIVITY_ROW             -> ActivityRowComponent
SduiComponentType.EXCLUSIVE_OFFER_BANNER   -> ExclusiveOfferBanner
SduiComponentType.SECTION_HEADER_ROW       -> SectionHeaderRow
SduiComponentType.ACCOUNT_DETAIL_CARD      -> AccountDetailCard
SduiComponentType.BALANCE_HERO             -> BalanceHeroComponent
SduiComponentType.SOURCE_ACCOUNT_SELECTOR  -> SourceAccountSelector
SduiComponentType.AMOUNT_INPUT_CARD        -> AmountInputCard
SduiComponentType.BENEFICIARY_GRID         -> BeneficiaryGridComponent
SduiComponentType.TRANSFER_LIMIT_BANNER    -> TransferLimitBanner
SduiComponentType.PROFILE_INFO_CARD        -> ProfileInfoCard
SduiComponentType.PROFILE_SETTINGS_CARD    -> ProfileSettingsCard
SduiComponentType.AVATAR_SECTION           -> AvatarSectionComponent
SduiComponentType.ADD_BENEFICIARY_FORM     -> AddBeneficiaryForm
SduiComponentType.VERIFICATION_CARD        -> VerificationCard

## ── RULES ────────────────────────────────────────────────────────────────
# 1. All composables in :core:ui — zero feature-module code
# 2. All composables stateless — receive props + onAction only
# 3. Each composable MUST have @Preview
# 4. Register in ComponentRegistry BEFORE generating any feature JSON
# 5. ARCH_HEADER is used on EVERY screen — never hardcode a top bar in features
# 6. Every unimplemented action MUST show Toast "Not implemented yet" — never crash
# 7. All card themes (bg color, image) driven from server JSON props
# 8. BENEFICIARY_GRID column count driven from server JSON — default 2
