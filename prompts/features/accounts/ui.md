# Accounts Tab — UI Layer
# cat with: CONTEXT.md + contract/mvi.md + contract/sdui_contract.md
#           + contract/renderer.md + composite/composite_components.md
#           + composite/loading_skeleton.md + features/accounts/feature.md + this file
#
# Screenshot: app/src/main/assets/screenshots/Accounts.png
# Claude Code MUST open Accounts.png before writing any code.
# Match every pixel — card styles, colors, spacing, button shapes, dark/light variants.

## Generate these files ONLY
- AccountsScreen.kt
- AccountsViewModel.kt
- AccountsState.kt  (AccountsState + AccountsIntent + AccountsEffect)
- AccountsModule.kt
- assets/mock/screens/accounts_screen.json  ← full SDUI JSON defined in feature.md

## AccountsScreen.kt

@Composable
fun AccountsScreen(
    navController: NavController,
    viewModel: AccountsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AccountsEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                is AccountsEffect.NavigateToAccountDetail ->
                    Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // LOADING BOX — show skeleton while screenModel is null
    if (state.isLoading || state.screenModel == null) {
        ScreenLoadingBox(modifier = Modifier.fillMaxSize())
    } else {
        SDUIRenderer(
            screenModel = state.screenModel!!,
            onAction = { actionId ->
                viewModel.handleIntent(AccountsIntent.HandleAction(actionId))
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

## AccountsViewModel.kt
- Inherits BaseViewModel<AccountsState, AccountsIntent, AccountsEffect>
- init { handleIntent(AccountsIntent.LoadScreen) }
- All Manage/Statement actions → setEffect(ShowToast("Not implemented yet"))
- Has @Preview with mock AccountsState

## AccountsModule.kt (Hilt)
@Module @InstallIn(ViewModelComponent::class)
Provides:
- AccountsRepository binding (interface → impl)
- AccountsApiService
- GetAccountsUseCase
- GetAccountTransactionsUseCase

## SDUI JSON — assets/mock/screens/accounts_screen.json
# Full JSON is defined in features/accounts/feature.md
# Copy it exactly — do not alter any field names, values, or action IDs

## Pixel-Perfect Implementation Notes
# Open Accounts.png and verify each item below while coding:

### Header (ARCH_HEADER)
- White bg, Architect avatar logo left
- Bell icon right only — no search icon
- Same header height as all other screens

### Balance Hero (BALANCE_HERO)
- "TOTAL COMBINED BALANCE": 11sp Uppercase #6B7280, horizontalPadding 20dp
- "$1,248,392.42": 32sp Bold #1B2A5E, marginTop 4dp
- Change badge: rounded pill, bg #DCFCE7, text "+2.4% vs last month" #22C55E 12sp
  - Up arrow icon 10dp left of text inside badge
  - Badge padding: horizontal 8dp, vertical 4dp
  - marginTop 8dp below amount

### Account Card 1 — Elite Checking (LIGHT style)
- White card: 12dp cornerRadius, padding 16dp, margin horizontal 16dp
- Top row: icon square LEFT + card tag "•••• 8821" RIGHT (#9CA3AF 12sp)
  - Icon square: 40dp × 40dp, 10dp radius, bg NavyPrimary #1B2A5E, bank icon white 20dp
- "Elite Checking": 18sp SemiBold #1A1A1A, marginTop 12dp
- "Primary operating account": 13sp Regular #6B7280, marginTop 2dp
- Divider: 1dp #F0F1F3, marginTop 12dp
- "CURRENT BALANCE": 11sp Uppercase #6B7280
- "$42,905.12": 20sp Bold #1A1A1A, marginTop 4dp
- "AVAILABLE BALANCE": 11sp Uppercase #6B7280, marginTop 12dp
- "$41,500.00": 16sp SemiBold TealAccent #00C8A0, marginTop 4dp
- "Manage →" button: full width, height 44dp, 10dp radius
  - bg: #E6FAF6 (TealLight), border: 1dp #00C8A0, text: "Manage →" TealAccent 15sp Medium
  - marginTop 16dp

### Account Card 2 — High-Yield Savings (LIGHT style)
- Same base card as Elite Checking
- Icon square bg: TealAccent #00C8A0, piggy bank icon white
- "High-Yield Savings": 18sp SemiBold — followed by "6.85% APY" badge inline right
  - Badge: bg TealAccent, text white 11sp Bold, padding 4dp horizontal 6dp vertical, 4dp radius
- "Wealth Reserve Fund": 13sp #6B7280
- "TOTAL BALANCE" → "$285,487.30": same style as card 1
- "INTEREST EARNED YTD" → "+$9,214.50": GreenSuccess #22C55E 16sp SemiBold
- Same teal Manage button

### Account Card 3 — Wealth Management (DARK style)
- NavyPrimary #1B2A5E bg card: 12dp radius, padding 16dp, margin horizontal 16dp
- Top row: icon square + "•••• 0012" WHITE 12sp right
  - Icon square: 40dp × 40dp, 10dp radius, bg rgba(255,255,255,0.15), wallet icon white
- "Wealth Management": 18sp SemiBold WHITE
- "Managed Assets Portfolio": 13sp rgba(255,255,255,0.7)
- Divider: 1dp rgba(255,255,255,0.2), marginTop 12dp
- "TOTAL PORTFOLIO VALUE": 11sp Uppercase rgba(255,255,255,0.7)
- "$920,000.00": 20sp Bold WHITE, marginTop 4dp
- Equity/Cash two-column row, marginTop 16dp:
  - Left: "EQUITY" 10sp rgba(255,255,255,0.7) + "72%" 16sp Bold WHITE + "$692.4k" 12sp rgba(255,255,255,0.7)
  - Right: "CASH" 10sp rgba(255,255,255,0.7) + "28%" 16sp Bold WHITE + "$257.6k" 12sp rgba(255,255,255,0.7)
- "Manage Portfolio →" button: full width, height 44dp, 10dp radius
  - bg: transparent, border: 1.5dp WHITE, text: "Manage Portfolio →" WHITE 15sp Medium
  - marginTop 16dp

### Recent Activity Section
- "Recent Activity": 20sp SemiBold #1A1A1A, horizontalPadding 20dp
- "Real-time updates across all assets": 13sp #6B7280, marginTop 2dp
- "View Statement": 13sp BlueLink #2563EB — right-aligned same row as title
- ACTIVITY_ROW items:
  - Row height: 60dp, horizontalPadding 20dp
  - Icon square: 40dp × 40dp, 8dp radius, bg per iconBgColor prop
  - Title: 14sp Medium #1A1A1A
  - Subtitle: 12sp Regular #6B7280 (ACCOUNT TYPE • CATEGORY • DATE)
  - Amount: 14sp SemiBold, right-aligned
    - Positive: GreenSuccess #22C55E
    - Negative: #1A1A1A
  - 1dp divider #F0F1F3 between rows

## Design Tokens
BackgroundGrey:       #F5F6F8  → screen bg
White:                #FFFFFF  → light cards
NavyPrimary:          #1B2A5E  → dark card bg, checking icon bg, nav selected
TealAccent:           #00C8A0  → savings icon bg, APY badge, available balance, manage border
TealLight:            #E6FAF6  → manage button bg (light cards)
GreenSuccess:         #22C55E  → positive amounts, change badge text
BlueLink:             #2563EB  → "View Statement" link
TextPrimary:          #1A1A1A  → main balances, account names
TextSecondary:        #6B7280  → subtitles, labels
TextTertiary:         #9CA3AF  → card tags, captions
DividerColor:         #F0F1F3  → card internal dividers
