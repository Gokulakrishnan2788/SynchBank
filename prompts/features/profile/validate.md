# Validate — Profile Tab
# cat with: CONTEXT.md + features/validate.md + features/profile/feature.md + this file

## UI Pixel Accuracy (open _User_Profile.png and verify)
[ ] Avatar: ~80dp square, 12dp corner radius, edit pencil badge bottom-right
[ ] Name: 24sp bold, member since 11sp uppercase grey below
[ ] Personal info card: white card, two fields with divider between
[ ] Field label: 11sp uppercase grey, value: 14sp dark
[ ] Security card: rows with teal subtle icon squares (36dp, 8dp radius)
[ ] Biometric row: fingerprint icon, sublabel "Use FaceID or TouchID", navy toggle right
[ ] App Preferences card: three rows, Language row has trailing text label (no chevron)
[ ] Log Out button: white bg, RED border 1.5dp, red text, full width
[ ] Footer: "ARCHITECT V4.2.0 • SECURED BY DEEP LEDGER" centered tiny grey
[ ] Bottom nav: PROFILE pill dark navy selected

## Behaviour
[ ] Logout → confirmation dialog first → LogoutUseCase → navigate to LOGIN
[ ] NavigateToLogin: popUpTo LOGIN inclusive (back stack fully cleared)
[ ] Biometric toggle → optimistic state update
[ ] All other rows → Toast "Not implemented yet"
[ ] No action crashes app

## Architecture
[ ] ProfileScreen uses SDUIRenderer only
[ ] ProfileViewModel extends BaseViewModel
[ ] LogoutUseCase not duplicated from :feature:login

## Build
[ ] ./gradlew :feature:profile:compileDebugKotlin — zero errors
[ ] ./gradlew :feature:profile:testDebugUnitTest — zero failures

## Output format
FAIL: file + line + fix | PASS: ✅ | Summary: X/Y passed
