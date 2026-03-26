# Feature: Login
# Master overview — load this alongside specific layer files

## Design Reference
Figma: https://www.figma.com/design/A1RAH1kkTIiCI2wYgDXn9V/Mobile-Team-Assignment
Screen: "Secure Portal" login screen (see Login_Screen.png)

## UI Description (from design)
- Top: Architect logo (top-left)
- Large bold heading: "Secure Portal" (2 lines)
- Subtitle: "Access your institutional wealth management dashboard."
- USERNAME field: "Institutional ID or Email"
- PASSWORD field: with show/hide toggle + "FORGOT?" link on right
- LOGIN button: full-width, navy primary
- AUTHENTICATION section: Fingerprint + Face ID icon buttons
- Footer: "New to the firm? Inquire about an account" (link text)
- Background: light grey (#F5F6F8)

## Color Tokens to Use
NavyPrimary: #1B2A5E
BackgroundGrey: #F5F6F8
TextPrimary: #1A1A1A
TextSecondary: #6B7280
InputBackground: #EAECF0

## Module: :feature:login
## Files to generate (spread across layer prompts):
API layer:     LoginApiService, LoginRequestDto, LoginResponseDto
DB layer:      SessionEntity, SessionDao (if not exists)
Domain layer:  LoginUseCase, LogoutUseCase, LoginRepository (interface + impl)
UI layer:      LoginScreen, LoginViewModel, LoginState/Intent/Effect, LoginModule
Assets:        assets/mock/screens/login_screen.json
               assets/mock/api/auth_login.json

## Feature Rules
- Screen is 100% SDUI — no hardcoded Composables for layout
- Biometric auth triggers system BiometricPrompt via BiometricUseCase
- Token stored in SessionDao after successful login
- "FORGOT?" triggers FORGOT_PASSWORD NavigationAction from JSON
- "Inquire" triggers deep link architect://onboarding/inquire
