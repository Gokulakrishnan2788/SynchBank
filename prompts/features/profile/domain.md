# Profile — Domain Layer
# cat with: CONTEXT.md + contract/domain_contract.md + features/profile/feature.md + this file

## Generate these files ONLY
- ProfileRepository.kt (interface in :core:domain)
- ProfileRepositoryImpl.kt (in :feature:profile)
- GetUserProfileUseCase.kt
- UpdateBiometricSettingUseCase.kt
- LogoutUseCase.kt (ONLY if not already in :feature:login)
- ProfileMapper.kt
- UserProfile.kt (domain model)

## ProfileRepository Interface
interface ProfileRepository {
    suspend fun getUserProfile(): Result<UserProfile>
    suspend fun updateBiometricEnabled(enabled: Boolean): Result<UserProfile>
    suspend fun logout(): Result<Unit>
}

## Domain Model
data class UserProfile(
    val userId: String, val name: String, val memberSince: String,
    val email: String, val phone: String, val avatarAsset: String?,
    val biometricEnabled: Boolean, val appVersion: String, val securedBy: String
)

## Rules
- Check for LogoutUseCase in :feature:login before generating — NEVER duplicate
- Pure Kotlin domain model
- ProfileRepositoryImpl in :feature:profile
