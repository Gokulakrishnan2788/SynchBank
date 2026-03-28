# Add Beneficiary — UI Layer
# cat with: CONTEXT.md + contract/mvi.md + contract/sdui_contract.md
#           + contract/renderer.md + composite/composite_components.md
#           + composite/loading_skeleton.md + this file
# Screenshot: app/src/main/assets/screenshots/Add_Beneficiary.png
# Claude Code MUST open Add_Beneficiary.png before writing any code.
# Match every pixel — header with back+avatar, form card fields, verification card layout.

## Generate these files ONLY
- AddBeneficiaryScreen.kt
- AddBeneficiaryViewModel.kt
- AddBeneficiaryState.kt (AddBeneficiaryState + AddBeneficiaryIntent + AddBeneficiaryEffect)
- assets/mock/screens/add_beneficiary_screen.json ← already defined in api.md

## AddBeneficiaryScreen.kt

@Composable
fun AddBeneficiaryScreen(
    navController: NavController,
    viewModel: AddBeneficiaryViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Handle system back press
    BackHandler {
        viewModel.handleIntent(AddBeneficiaryIntent.NavigateBack)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is AddBeneficiaryEffect.NavigateBack ->
                    navController.popBackStack()
                is AddBeneficiaryEffect.ShowSuccess -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
                is AddBeneficiaryEffect.ShowToast ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                is AddBeneficiaryEffect.ShowError ->
                    Toast.makeText(context, effect.message, Toast.LENGTH_LONG).show()
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
                viewModel.handleIntent(AddBeneficiaryIntent.HandleAction(actionId))
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}

## AddBeneficiaryViewModel.kt
- Inherits BaseViewModel<AddBeneficiaryState, AddBeneficiaryIntent, AddBeneficiaryEffect>
- @HiltViewModel
- init { handleIntent(AddBeneficiaryIntent.LoadScreen) }
- Inject: AddBeneficiaryUseCase, GetBankInstitutionsUseCase
- isSubmitting=true during Submit → button shows loading spinner
- Field-level validation errors shown inline below each field
- All validation delegated to AddBeneficiaryUseCase — zero inline validation

## Pixel-Perfect Implementation Notes
# Open Add_Beneficiary.png and verify each item while coding:

### Header (ARCH_HEADER — showBack=true, showAvatar=true)
- Back arrow (←) left: 24dp icon, NavyPrimary #1B2A5E, 48dp tap target
- "Architect" title: centered, 18sp SemiBold #1B2A5E
- Bell icon right: 24dp outlined grey
- Avatar icon right of bell: circular 32dp, user avatar image
- White header bg, no shadow/elevation
- Horizontal padding 16dp

### Page Header
- "SECURITY & IDENTITY": 11sp Bold Uppercase TealAccent #00C8A0
  - marginTop 24dp from header
- "Add Beneficiary": 28sp Bold #1A1A1A, marginTop 8dp
- Subtitle text: 14sp Regular #6B7280, marginTop 8dp, lineHeight 22sp

### Form Card
- White bg, 12dp cornerRadius, 1dp border #F0F1F3, padding 16dp
- margin horizontal 16dp, marginTop 28dp

#### Each input field:
- Label: 11sp Bold Uppercase #9CA3AF, marginBottom 6dp
- Field bg: #F5F6F8
- Field border: 1dp #E5E7EB, 8dp cornerRadius
- Field height: 48dp, horizontal padding 12dp
- Placeholder text: 15sp Regular #9CA3AF
- Active/focused border: 1.5dp TealAccent #00C8A0
- Error border: 1.5dp RedDestructive #EF4444
- Helper text: 12sp Regular #9CA3AF, marginTop 4dp
- Error text: 12sp Regular #EF4444, marginTop 4dp
- Gap between fields: 16dp

#### BANK NAME dropdown:
- Shows "Select Institution" as placeholder with chevron (↓) right
- On tap: opens ModalBottomSheet with list of banks
- Each bank row: bank name 15sp, optional logo left

### Verification Card
- White bg, 12dp cornerRadius, 1dp border #F0F1F3, padding 16dp
- margin horizontal 16dp, marginTop 24dp
- Row layout: icon left + text middle + illustration right (partially clipped)
  - Icon container: 48dp×48dp, 12dp radius, TealLight #E0F7F4 bg
    - Shield checkmark icon: 28dp TealAccent #00C8A0
  - Text block marginLeft 12dp:
    - "Secure Verification": 16sp SemiBold #1A1A1A
    - Description: 13sp Regular #6B7280, marginTop 4dp, lineHeight 18sp
  - Illustration: decorative, right-aligned, partially visible (overflow hidden)

### Save Beneficiary Button
- Full width, NavyPrimary #1B2A5E bg, white text "Save Beneficiary" 16sp SemiBold
- Height: 56dp, cornerRadius 12dp
- margin horizontal 16dp, marginTop 24dp
- When isSubmitting=true: show CircularProgressIndicator (white, 20dp) in center

### Bottom Nav
- Shows PAYMENTS tab selected (dark navy pill)
- Same bottom nav as Transfer screen

## SDUI JSON Reference
# Full JSON defined in add_beneficiary/api.md → assets/mock/screens/add_beneficiary_screen.json
# Copy it exactly — do not alter field names, types, or action IDs

## Design Tokens
BackgroundGrey:   #F5F6F8  → screen bg, field bg
White:            #FFFFFF  → card backgrounds
NavyPrimary:      #1B2A5E  → back arrow, title, save button bg
TealAccent:       #00C8A0  → SECURITY label, shield icon, active field border
TealLight:        #E0F7F4  → shield icon container bg
TextPrimary:      #1A1A1A  → page title, field values, Secure Verification title
TextSecondary:    #6B7280  → page subtitle, description body text
TextTertiary:     #9CA3AF  → field labels, placeholders, helper text
CardBorder:       #F0F1F3  → form card and verification card border
FieldBorder:      #E5E7EB  → input field default border
RedDestructive:   #EF4444  → error field border, error text
