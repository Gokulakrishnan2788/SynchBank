# Accounts — API Layer
# cat with: CONTEXT.md + contract/api_contract.md + features/accounts/feature.md + this file
# feature.md contains the full mock JSON and DTO definitions — read it first.

## Generate these files ONLY
- AccountsApiService.kt
- AccountDetailDto.kt (AccountDetailDto, AccountsResponseDto, AccountsListDto)
- assets/mock/api/accounts_list.json    ← content defined in feature.md
- assets/mock/screens/accounts_screen.json ← content defined in feature.md

## Endpoints
GET /accounts          → AccountsListDto
GET /screens/accounts  → ScreenResponse (SDUI JSON)

## Rules
- Do NOT create new NetworkModule or Retrofit instance
- Inject AccountsApiService via AccountsModule.kt
- Reuse ActivityItemDto if already exists in :feature:dashboard
