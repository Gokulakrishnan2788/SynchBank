# Home (Dashboard) — Domain Layer
# cat with: CONTEXT.md + contract/domain_contract.md + this file

## Generate these files ONLY
- DashboardRepository.kt (interface in :core:domain)
- DashboardRepositoryImpl.kt (in :feature:dashboard)
- GetDashboardUseCase.kt
- GetChartDataUseCase.kt
- DashboardMapper.kt
- DashboardData.kt (domain model)
- AccountSummary.kt (domain model — if not exists)
- ActivityItem.kt (domain model)
- ExclusiveOffer.kt (domain model)
- ChartData.kt (domain model)

## DashboardRepository Interface
interface DashboardRepository {
    suspend fun getDashboard(): Result<DashboardData>
    suspend fun getChartData(period: String): Result<ChartData>
    suspend fun getHomeScreen(): Result<ScreenModel>
}

## GetDashboardUseCase
Params: NoParams
Returns: Result<DashboardData>

## GetChartDataUseCase
Params: data class Params(val period: String)
Validation: period must not be blank
Returns: Result<ChartData>

## Domain Models
data class DashboardData(
    val netWorth: Double,
    val netWorthChange: String,
    val netWorthChangePositive: Boolean,
    val accounts: List<AccountSummary>,
    val activity: List<ActivityItem>,
    val exclusiveOffer: ExclusiveOffer
)

data class AccountSummary(
    val id: String,
    val type: String,
    val balance: String,
    val detail: String,
    val iconAsset: String
)

data class ActivityItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val amountPositive: Boolean,
    val iconAsset: String,
    val iconBgColor: String
)

data class ExclusiveOffer(
    val badgeLabel: String,
    val title: String,
    val ctaLabel: String,
    val bgColor: String,
    val illustrationAsset: String
)

data class ChartData(
    val period: String,
    val dataPoints: List<Double>
)

## Rules
- Pure Kotlin domain models — no @Serializable, no @Entity, no Android imports
- DashboardRepositoryImpl in :feature:dashboard
- Interface DashboardRepository in :core:domain
