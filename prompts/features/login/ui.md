# Login — UI Layer
# cat with: CONTEXT.md + base/mvi.md + base/sdui_contract.md + base/renderer.md + this file

## Generate these files ONLY
- LoginScreen.kt
- LoginViewModel.kt
- LoginState.kt (contains LoginState + LoginIntent + LoginEffect)
- LoginModule.kt
- assets/mock/screens/login_screen.json (full SDUI schema)

## LoginScreen.kt
- Uses SDUIRenderer — NO hardcoded Composable layout
- Collects state via collectAsStateWithLifecycle()
- Handles LoginEffect in LaunchedEffect
- BiometricEffect → launches BiometricPrompt via BiometricManager
- Passes form field values from state to SDUI via FormState wrapper
- Has @Preview with mock state

## LoginViewModel.kt
- Inherits BaseViewModel<LoginState, LoginIntent, LoginEffect>
- On init: dispatch LoadScreen intent
- Handles all LoginIntents per contract.md
- Validates via LoginUseCase (not inline)
- Uses GetSessionUseCase on init to auto-navigate if session exists

## LoginModule.kt (Hilt)
@Module @InstallIn(ViewModelComponent::class)
Provides:
- LoginRepository binding (interface → impl)
- LoginApiService
- LoginUseCase
- LogoutUseCase
- GetSessionUseCase

## SDUI JSON
Generate complete login_screen.json matching the design:
- Logo, title, subtitle
- Username TEXT_FIELD (email validation)
- Password TEXT_FIELD (PASSWORD type, show/hide, FORGOT action)
- Login BUTTON (PRIMARY, SUBMIT_FORM action)
- BIOMETRIC_ROW (FINGERPRINT + FACE_ID)
- LINK_TEXT for inquire
All actions wired per sdui_contract.md

## Design Tokens to Apply in SDUI JSON props
background: "BackgroundGrey"
primaryButton: "NavyPrimary"
title style: "DISPLAY"
subtitle style: "BODY"
label style: "LABEL"
