# Profile Tab — UI Layer
# cat with: CONTEXT.md + contract/mvi.md + contract/sdui_contract.md
#           + contract/renderer.md + composite/composite_components.md
#           + composite/loading_skeleton.md + this file
# Screenshot: app/src/main/assets/screenshots/_User_Profile.png
# Claude Code MUST open _User_Profile.png before writing any code.

## Generate these files ONLY
- ProfileScreen.kt
- ProfileViewModel.kt
- ProfileState.kt  (ProfileState + ProfileIntent + ProfileEffect)
- ProfileModule.kt
- assets/mock/screens/profile_screen.json
- assets/mock/api/user_profile.json

## ProfileScreen.kt
- Hosted inside MainScreen's NavHost at route "profile"
- Uses SDUIRenderer — NO hardcoded Composable layout
- On init dispatches ProfileIntent.LoadScreen
- Collects state via collectAsStateWithLifecycle()
- PROFILE_ROW with trailingType=TOGGLE: wires toggle callback to ProfileIntent.BiometricToggled
- Log Out button: shows confirmation dialog via Effect, on confirm dispatches ProfileIntent.LogOut
- NavigateToLogin effect: navController.navigate(Routes.LOGIN) with popUpTo clearing back stack
- Has @Preview with mock ProfileState

## ProfileViewModel.kt
- Inherits BaseViewModel<ProfileState, ProfileIntent, ProfileEffect>
- init { handleIntent(ProfileIntent.LoadScreen) }
- Biometric toggle updates optimistically in state before API call settles

## ProfileModule.kt (Hilt)
@Module @InstallIn(ViewModelComponent::class)
Provides:
- ProfileRepository binding (interface → impl)
- ProfileApiService
- GetUserProfileUseCase
- UpdateBiometricSettingUseCase
- LogoutUseCase  (bind only if not already bound in LoginModule)

## SDUI JSON — profile_screen.json
{
  "screenId": "profile",
  "version": "1.0",
  "metadata": { "title": "Profile", "analyticsTag": "profile_screen" },
  "layout": { "type": "SCROLL", "padding": { "horizontal": 24, "vertical": 32 } },
  "components": [
    {
      "id": "avatar_header",
      "type": "AVATAR_HEADER",
      "props": {
        "avatarUrl": null,
        "avatarAsset": "avatar_alexander",
        "name": "Alexander Sterling",
        "memberSince": "MEMBER SINCE JANUARY 2022",
        "editAction": "EDIT_AVATAR"
      }
    },
    { "id": "spacer_1", "type": "SPACER", "props": { "height": 32 } },
    {
      "id": "personal_info_label",
      "type": "TEXT",
      "props": { "text": "PERSONAL INFORMATION", "style": "LABEL", "color": "TextSecondary" }
    },
    { "id": "spacer_2", "type": "SPACER", "props": { "height": 12 } },
    {
      "id": "email_field",
      "type": "INFO_FIELD",
      "props": {
        "label": "PRIMARY EMAIL",
        "value": "a.sterling@architect.com"
      }
    },
    { "id": "divider_1", "type": "DIVIDER", "props": { "thickness": 1, "color": "DividerColor" } },
    {
      "id": "phone_field",
      "type": "INFO_FIELD",
      "props": {
        "label": "PHONE NUMBER",
        "value": "+1 (555) 892-4410"
      }
    },
    { "id": "spacer_3", "type": "SPACER", "props": { "height": 32 } },
    {
      "id": "security_label",
      "type": "TEXT",
      "props": { "text": "SECURITY & ACCESS", "style": "LABEL", "color": "TextSecondary" }
    },
    { "id": "spacer_4", "type": "SPACER", "props": { "height": 12 } },
    {
      "id": "change_password_row",
      "type": "PROFILE_ROW",
      "props": {
        "iconAsset": "ic_lock_refresh",
        "label": "Change Password",
        "sublabel": null,
        "trailingType": "ARROW",
        "trailingValue": null,
        "toggleEnabled": null
      },
      "action": "CHANGE_PASSWORD"
    },
    { "id": "divider_2", "type": "DIVIDER", "props": { "thickness": 1, "color": "DividerColor" } },
    {
      "id": "biometric_row",
      "type": "PROFILE_ROW",
      "props": {
        "iconAsset": "ic_fingerprint",
        "label": "Biometric Login",
        "sublabel": "Use FaceID or TouchID",
        "trailingType": "TOGGLE",
        "trailingValue": null,
        "toggleEnabled": true
      },
      "action": "BIOMETRIC_TOGGLE"
    },
    { "id": "spacer_5", "type": "SPACER", "props": { "height": 32 } },
    {
      "id": "preferences_label",
      "type": "TEXT",
      "props": { "text": "APP PREFERENCES", "style": "LABEL", "color": "TextSecondary" }
    },
    { "id": "spacer_6", "type": "SPACER", "props": { "height": 12 } },
    {
      "id": "notifications_row",
      "type": "PROFILE_ROW",
      "props": {
        "iconAsset": "ic_bell",
        "label": "Push Notifications",
        "sublabel": null,
        "trailingType": "ARROW",
        "trailingValue": null,
        "toggleEnabled": null
      },
      "action": "PUSH_NOTIFICATIONS"
    },
    { "id": "divider_3", "type": "DIVIDER", "props": { "thickness": 1, "color": "DividerColor" } },
    {
      "id": "appearance_row",
      "type": "PROFILE_ROW",
      "props": {
        "iconAsset": "ic_moon",
        "label": "Appearance",
        "sublabel": "System Default",
        "trailingType": "ARROW",
        "trailingValue": null,
        "toggleEnabled": null
      },
      "action": "APPEARANCE"
    },
    { "id": "divider_4", "type": "DIVIDER", "props": { "thickness": 1, "color": "DividerColor" } },
    {
      "id": "language_row",
      "type": "PROFILE_ROW",
      "props": {
        "iconAsset": "ic_globe",
        "label": "Language",
        "sublabel": null,
        "trailingType": "LABEL",
        "trailingValue": "English (US)",
        "toggleEnabled": null
      },
      "action": "LANGUAGE"
    },
    { "id": "spacer_7", "type": "SPACER", "props": { "height": 40 } },
    {
      "id": "logout_btn",
      "type": "BUTTON",
      "props": { "label": "Log Out", "style": "DESTRUCTIVE", "loading": false },
      "action": "LOG_OUT"
    },
    { "id": "spacer_8", "type": "SPACER", "props": { "height": 16 } },
    {
      "id": "app_version_text",
      "type": "TEXT",
      "props": { "text": "ARCHITECT V4.2.0 • SECURED BY DEEP LEDGER", "style": "CAPTION", "color": "TextTertiary" }
    },
    { "id": "spacer_end", "type": "SPACER", "props": { "height": 80 } }
  ],
  "actions": {
    "EDIT_AVATAR":         { "type": "SHOW_DIALOG", "params": { "dialogType": "AVATAR_PICKER" } },
    "CHANGE_PASSWORD":     { "type": "NAVIGATE", "destination": "change_password" },
    "BIOMETRIC_TOGGLE":    { "type": "API_CALL", "endpoint": "/profile/biometric", "method": "POST" },
    "PUSH_NOTIFICATIONS":  { "type": "NAVIGATE", "destination": "push_notifications" },
    "APPEARANCE":          { "type": "NAVIGATE", "destination": "appearance" },
    "LANGUAGE":            { "type": "NAVIGATE", "destination": "language" },
    "LOG_OUT":             { "type": "SHOW_DIALOG", "params": { "message": "Are you sure you want to log out?", "confirmAction": "CONFIRM_LOGOUT" } },
    "CONFIRM_LOGOUT":      { "type": "API_CALL", "endpoint": "/auth/logout", "method": "POST",
                             "onSuccess": { "type": "NAVIGATE", "destination": "login" },
                             "onError":   { "type": "SHOW_DIALOG", "params": { "message": "Logout failed. Please try again." } } }
  }
}

## Design Tokens
background: "BackgroundGrey"
avatarBorder: "NavyPrimary"
sectionLabel: "TextSecondary"
rowBackground: "White"
divider: "DividerColor"
toggleActive: "NavyPrimary"
logoutButton: "DestructiveRed"  → style DESTRUCTIVE maps to outlined red border, red text
appVersion: "TextTertiary"
