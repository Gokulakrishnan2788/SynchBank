# Profile — MVI Contract
# Full contract defined in features/profile/feature.md
# cat with: CONTEXT.md + contract/mvi.md + features/profile/feature.md + this file

## Additional Notes
- ProfileState, ProfileIntent, ProfileEffect all in feature.md
- Logout MUST show confirm dialog before executing
- NavigateToLogin effect: popUpTo(Routes.LOGIN) inclusive=true clears back stack
- Biometric toggle: optimistic state update before API settles
