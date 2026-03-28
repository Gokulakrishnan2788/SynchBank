# Accounts — Domain Layer
# cat with: CONTEXT.md + contract/domain_contract.md + features/accounts/feature.md + this file
# feature.md contains domain model definitions — read it first.

## Generate these files ONLY
- AccountsRepository.kt (interface in :core:domain)
- AccountsRepositoryImpl.kt (in :feature:accounts)
- GetAccountsUseCase.kt
- GetAccountTransactionsUseCase.kt
- AccountsMapper.kt
- AccountDetail.kt (domain model — if not exists)

## AccountsRepository Interface
interface AccountsRepository {
    suspend fun getAccounts(): Result<AccountsListData>
    suspend fun getRecentActivity(): Result<List<ActivityItem>>
    suspend fun getAccountsScreen(): Result<ScreenModel>
}

## Rules
- AccountDetail and ActivityItem: pure Kotlin, no Android imports
- Reuse ActivityItem from :core:domain if already exists
- AccountsRepositoryImpl in :feature:accounts
- Interface in :core:domain
