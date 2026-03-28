# Feature: Profile Tab
# Screenshot: app/src/main/assets/screenshots/_User_Profile.png
# Claude Code MUST open _User_Profile.png before generating any UI code.

## Screen Reference
Route: "profile" inside MainScreen NavHost

## Pixel-Perfect UI Description

### Header
- ARCH_HEADER: showSearch=false, showNotification=true, showBack=false

### Avatar Section — AVATAR_SECTION component
- Square avatar ~80dp, cornerRadius 12dp
- Small edit pencil badge bottom-right (dark circle ~20dp, pencil icon white)
- "Alexander Sterling" — 24sp bold #1A1A1A
- "MEMBER SINCE JANUARY 2022" — 11sp uppercase #9CA3AF, margin-top 4dp

### Personal Information — PROFILE_INFO_CARD
- Section label: "PERSONAL INFORMATION" — 11sp uppercase #6B7280, margin-bottom 8dp
- White card 12dp radius:
  - Row 1: "PRIMARY EMAIL" 11sp grey label / "a.sterling@architect.com" 14sp #1A1A1A
  - 1dp divider #E5E7EB
  - Row 2: "PHONE NUMBER" 11sp grey label / "+1 (555) 892-4410" 14sp #1A1A1A
  - Each field: 16dp horizontal padding, 14dp vertical padding

### Security & Access — PROFILE_SETTINGS_CARD
- Section label: "SECURITY & ACCESS" — 11sp uppercase #6B7280
- White card 12dp radius:
  - Row 1: lock-refresh icon (teal subtle bg square 36dp, 8dp radius) / "Change Password" 15sp / chevron right #9CA3AF
  - 1dp divider
  - Row 2: fingerprint icon (teal subtle bg) / "Biometric Login" 15sp / "Use FaceID or TouchID" 12sp grey sublabel / Navy toggle ON right

### App Preferences — PROFILE_SETTINGS_CARD
- Section label: "APP PREFERENCES" — 11sp uppercase #6B7280
- White card 12dp radius:
  - Row 1: bell icon (teal bg) / "Push Notifications" / chevron
  - 1dp divider
  - Row 2: moon icon (teal bg) / "Appearance" / "System Default" grey sublabel / chevron
  - 1dp divider
  - Row 3: globe icon (teal bg) / "Language" / "English (US)" grey trailing label (no chevron)

### Log Out Button
- Full width button, white bg, 1.5dp border #EF4444 (red), 12dp radius
- "→ Log Out" — red #EF4444 16sp, icon left

### Footer
- "ARCHITECT V4.2.0 • SECURED BY DEEP LEDGER" — 10sp centered #9CA3AF
- Margin top 16dp from logout button

## Color Tokens
NavyPrimary:     #1B2A5E  (toggle active)
TealSubtle:      #F0FDF9  (icon container bg — very light teal)
BackgroundGrey:  #F5F6F8
White:           #FFFFFF
TextPrimary:     #1A1A1A
TextSecondary:   #6B7280
TextTertiary:    #9CA3AF
RedDestructive:  #EF4444
DividerColor:    #E5E7EB

## Module: :feature:profile
## Files to generate:
API:     ProfileApiService, UserProfileDto, UpdateProfileRequestDto
         assets/mock/api/user_profile.json
         assets/mock/screens/profile_screen.json
Domain:  GetUserProfileUseCase, UpdateBiometricSettingUseCase, LogoutUseCase (if not exists),
         ProfileRepository (interface+impl), ProfileMapper, UserProfile (model)
UI:      ProfileScreen, ProfileViewModel, ProfileState/Intent/Effect, ProfileModule
         assets/mock/screens/profile_screen.json
Tests:   ProfileViewModelTest, GetUserProfileUseCaseTest, UpdateBiometricSettingUseCaseTest

## MVI Contract

### ProfileState
data class ProfileState(
    val isLoading: Boolean = false,
    val isLoggingOut: Boolean = false,
    val error: String? = null,
    val screenModel: ScreenModel? = null,
    val userProfile: UserProfile? = null,
    val biometricEnabled: Boolean = true
) : UiState

### ProfileIntent
sealed class ProfileIntent : UiIntent {
    object LoadScreen : ProfileIntent()
    data class BiometricToggled(val enabled: Boolean) : ProfileIntent()
    object LogOut : ProfileIntent()
    data class HandleAction(val actionId: String) : ProfileIntent()
    object ClearError : ProfileIntent()
}

### ProfileEffect
sealed class ProfileEffect : UiEffect {
    object NavigateToLogin : ProfileEffect()            // after logout — popUpTo LOGIN inclusive
    data class ShowToast(val message: String) : ProfileEffect()
    data class ShowLogoutConfirmDialog(val message: String) : ProfileEffect()
}

### Reducer
LoadScreen → GetUserProfileUseCase → setState(userProfile, biometricEnabled, screenModel)
BiometricToggled → UpdateBiometricSettingUseCase → setState(biometricEnabled)
HandleAction("CHANGE_PASSWORD")      → ShowToast("Not implemented yet")
HandleAction("PUSH_NOTIFICATIONS")   → ShowToast("Not implemented yet")
HandleAction("APPEARANCE")           → ShowToast("Not implemented yet")
HandleAction("LANGUAGE")             → ShowToast("Not implemented yet")
HandleAction("LOG_OUT")              → ShowLogoutConfirmDialog("Are you sure you want to log out?")
LogOut → isLoggingOut=true → LogoutUseCase → setEffect(NavigateToLogin)

## SDUI JSON — profile_screen.json
{
  "screenId": "profile",
  "version": "1.0",
  "metadata": { "title": "Profile", "analyticsTag": "profile_screen" },
  "layout": { "type": "SCROLL", "padding": { "horizontal": 20, "vertical": 0 } },
  "components": [
    { "id": "header", "type": "ARCH_HEADER", "props": { "logoAsset": "ic_architect_avatar", "title": "Architect", "showSearch": false, "showNotification": true, "showBack": false, "showAvatar": false } },
    { "id": "spacer_1", "type": "SPACER", "props": { "height": 24 } },
    { "id": "avatar", "type": "AVATAR_SECTION", "props": { "avatarAsset": "avatar_alexander", "name": "Alexander Sterling", "memberSince": "MEMBER SINCE JANUARY 2022", "editAction": "EDIT_AVATAR" } },
    { "id": "spacer_2", "type": "SPACER", "props": { "height": 28 } },
    { "id": "personal_label", "type": "TEXT", "props": { "text": "PERSONAL INFORMATION", "style": "LABEL", "color": "TextSecondary" } },
    { "id": "spacer_3", "type": "SPACER", "props": { "height": 8 } },
    { "id": "personal_info_card", "type": "PROFILE_INFO_CARD", "props": { "fields": [ { "label": "PRIMARY EMAIL", "value": "a.sterling@architect.com" }, { "label": "PHONE NUMBER", "value": "+1 (555) 892-4410" } ] } },
    { "id": "spacer_4", "type": "SPACER", "props": { "height": 24 } },
    { "id": "security_label", "type": "TEXT", "props": { "text": "SECURITY & ACCESS", "style": "LABEL", "color": "TextSecondary" } },
    { "id": "spacer_5", "type": "SPACER", "props": { "height": 8 } },
    { "id": "security_card", "type": "PROFILE_SETTINGS_CARD", "props": { "rows": [
      { "iconAsset": "ic_lock_refresh", "iconBgColor": "TealSubtle", "label": "Change Password", "sublabel": null, "trailingType": "ARROW", "action": "CHANGE_PASSWORD" },
      { "iconAsset": "ic_fingerprint", "iconBgColor": "TealSubtle", "label": "Biometric Login", "sublabel": "Use FaceID or TouchID", "trailingType": "TOGGLE", "toggleEnabled": true, "action": "BIOMETRIC_TOGGLE" }
    ] } },
    { "id": "spacer_6", "type": "SPACER", "props": { "height": 24 } },
    { "id": "prefs_label", "type": "TEXT", "props": { "text": "APP PREFERENCES", "style": "LABEL", "color": "TextSecondary" } },
    { "id": "spacer_7", "type": "SPACER", "props": { "height": 8 } },
    { "id": "prefs_card", "type": "PROFILE_SETTINGS_CARD", "props": { "rows": [
      { "iconAsset": "ic_bell", "iconBgColor": "TealSubtle", "label": "Push Notifications", "sublabel": null, "trailingType": "ARROW", "action": "PUSH_NOTIFICATIONS" },
      { "iconAsset": "ic_moon", "iconBgColor": "TealSubtle", "label": "Appearance", "sublabel": "System Default", "trailingType": "ARROW", "action": "APPEARANCE" },
      { "iconAsset": "ic_globe", "iconBgColor": "TealSubtle", "label": "Language", "sublabel": null, "trailingType": "LABEL", "trailingValue": "English (US)", "action": "LANGUAGE" }
    ] } },
    { "id": "spacer_8", "type": "SPACER", "props": { "height": 32 } },
    { "id": "logout_btn", "type": "BUTTON", "props": { "label": "Log Out", "style": "DESTRUCTIVE", "loading": false }, "action": "LOG_OUT" },
    { "id": "spacer_9", "type": "SPACER", "props": { "height": 16 } },
    { "id": "footer_text", "type": "TEXT", "props": { "text": "ARCHITECT V4.2.0 • SECURED BY DEEP LEDGER", "style": "CAPTION", "color": "TextTertiary", "textAlign": "CENTER" } },
    { "id": "spacer_end", "type": "SPACER", "props": { "height": 80 } }
  ],
  "actions": {
    "EDIT_AVATAR":          { "type": "SHOW_DIALOG", "params": { "message": "Not implemented yet" } },
    "CHANGE_PASSWORD":      { "type": "SHOW_DIALOG", "params": { "message": "Not implemented yet" } },
    "BIOMETRIC_TOGGLE":     { "type": "API_CALL", "endpoint": "/profile/biometric", "method": "POST" },
    "PUSH_NOTIFICATIONS":   { "type": "SHOW_DIALOG", "params": { "message": "Not implemented yet" } },
    "APPEARANCE":           { "type": "SHOW_DIALOG", "params": { "message": "Not implemented yet" } },
    "LANGUAGE":             { "type": "SHOW_DIALOG", "params": { "message": "Not implemented yet" } },
    "LOG_OUT":              { "type": "SHOW_DIALOG", "params": { "message": "Are you sure you want to log out?", "confirmAction": "CONFIRM_LOGOUT" } },
    "CONFIRM_LOGOUT":       { "type": "API_CALL", "endpoint": "/auth/logout", "method": "POST",
                              "onSuccess": { "type": "NAVIGATE", "destination": "login" },
                              "onError":   { "type": "SHOW_DIALOG", "params": { "message": "Logout failed. Please try again." } } }
  }
}

## Feature Rules
- Logout → confirm dialog first → LogoutUseCase → navigate to LOGIN with popUpTo clearing back stack
- Biometric toggle → optimistic state update before API settles
- LogoutUseCase: check :feature:login before generating — never duplicate
- All other rows → Toast "Not implemented yet" — never crash
- Screen 100% SDUI
