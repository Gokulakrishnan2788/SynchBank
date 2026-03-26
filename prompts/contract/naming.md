# Naming Conventions
# Apply to all generated files

## Files
XScreen.kt          → Composable screen entry point
XViewModel.kt       → MVI ViewModel
XState.kt           → State + Intent + Effect sealed classes (one file)
XRepository.kt      → Repository interface
XRepositoryImpl.kt  → Repository implementation
XUseCase.kt         → Single use case
XApiService.kt      → Retrofit service interface
XEntity.kt          → Room entity
XDao.kt             → Room DAO
XDto.kt             → API request/response data class
XMapper.kt          → DTO ↔ Domain model mapper
XModule.kt          → Hilt module

## Classes
- ViewModels:        LoginViewModel, DashboardViewModel
- States:            LoginState, DashboardState
- Intents:           LoginIntent, DashboardIntent
- Effects:           LoginEffect, DashboardEffect
- UseCases:          LoginUseCase, GetAccountsUseCase (verb + noun)
- Repositories:      LoginRepository (interface), LoginRepositoryImpl
- Entities:          SessionEntity, AccountEntity
- DTOs:              LoginRequestDto, LoginResponseDto
- SDUI Models:       ScreenModel, ComponentModel, ActionModel

## Functions
- Composables:       PascalCase — LoginScreen(), AccountCard()
- ViewModels:        camelCase — handleIntent(), reduceState()
- UseCases:          operator fun invoke() always
- Mappers:           toModel(), toEntity(), toDto()

## Constants
- Design tokens:     ArchitectColors.NavyPrimary, ArchitectSpacing.MD
- Routes:            Routes.LOGIN, Routes.DASHBOARD
- Actions:           SduiActions.SUBMIT_FORM, SduiActions.NAVIGATE

## Packages
com.architect.banking.core.ui
com.architect.banking.core.network
com.architect.banking.core.data
com.architect.banking.core.domain
com.architect.banking.engine.sdui
com.architect.banking.engine.navigation
com.architect.banking.feature.login
com.architect.banking.feature.dashboard
