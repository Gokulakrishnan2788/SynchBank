# SDUI Renderer Contract
# SDUIRenderer.kt and ComponentRegistry.kt are in :engine:sdui — DO NOT regenerate

## Adding a New Component Type (the ONLY allowed change to engine)
1. Add new type to SduiComponentType enum
2. Create new Composable in components/ folder
3. Register in ComponentRegistry:
   SduiComponentType.NEW_TYPE -> { props, onAction -> NewTypeComponent(props, onAction) }
4. Add props data class to ComponentProps.kt
Never edit existing registrations

## SDUIRenderer Usage in Screens
@Composable
fun XScreen(...) {
    SDUIRenderer(
        screenModel = state.screenModel,
        onAction = { actionId ->
            viewModel.handleIntent(XIntent.HandleAction(actionId))
        },
        modifier = Modifier.fillMaxSize()
    )
}

## ActionHandler Flow
User taps component with action="ACTION_ID"
    ↓
SDUIRenderer.onAction("ACTION_ID")
    ↓
ViewModel.handleIntent(XIntent.HandleAction("ACTION_ID"))
    ↓
ViewModel looks up action in screenModel.actions["ACTION_ID"]
    ↓
Executes ActionModel (API_CALL / NAVIGATE / etc.)
    ↓
On result: setState() or setEffect(NavigationEffect)

## Component Props Parsing
All props are JsonObject — use ComponentPropsParser to deserialize:
val textProps = props.parse<TextComponentProps>()
val buttonProps = props.parse<ButtonComponentProps>()

## Validation in TEXT_FIELD
TEXT_FIELD components with validations[] are validated by FormValidator
FormValidator.validate(fieldId, value, validations) → ValidationResult
Call before SUBMIT_FORM action

## Rules
- Never hardcode component rendering outside SDUIRenderer
- Never add feature-specific logic to SDUIRenderer
- Loading state: SDUIRenderer shows skeleton when screenModel is null
- Error state: SDUIRenderer shows error card when parsing fails
- All component Composables must be stateless — receive props + onAction only
