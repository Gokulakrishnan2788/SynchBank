# Profile — API Layer
# cat with: CONTEXT.md + contract/api_contract.md + features/profile/feature.md + this file
# feature.md contains full mock JSON and DTO definitions — read it first.

## Generate these files ONLY
- ProfileApiService.kt
- UserProfileDto.kt
- UpdateProfileRequestDto.kt
- assets/mock/api/user_profile.json
- assets/mock/screens/profile_screen.json ← content in feature.md

## Endpoints
GET  /profile              → UserProfileDto
POST /profile/biometric    → UserProfileDto
POST /auth/logout          → ApiResponse<Unit> (reuse LoginApiService if exists)

## Mock JSON — user_profile.json
{
  "success": true,
  "data": {
    "userId": "usr_001",
    "name": "Alexander Sterling",
    "memberSince": "JANUARY 2022",
    "email": "a.sterling@architect.com",
    "phone": "+1 (555) 892-4410",
    "avatarAsset": "avatar_alexander",
    "biometricEnabled": true,
    "appVersion": "V4.2.0",
    "securedBy": "DEEP LEDGER"
  }
}

## Rules
- Do NOT redeclare POST /auth/logout if already in LoginApiService
- Inject ProfileApiService via ProfileModule.kt
