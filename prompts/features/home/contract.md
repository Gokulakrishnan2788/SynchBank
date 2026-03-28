# Home (Dashboard) — MVI Contract

## DashboardState
data class DashboardState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val screenModel: ScreenModel? = null,
    val dashboardData: DashboardData? = null,
    val chartData: ChartData? = null,
    val selectedPeriod: String = "Last 6 Months",
    val isNetWorthVisible: Boolean = true
) : UiState

## DashboardIntent
sealed class DashboardIntent : UiIntent {
    object LoadScreen : DashboardIntent()
    object RefreshDashboard : DashboardIntent()
    object ToggleNetWorthVisibility : DashboardIntent()
    data class ChangePeriod(val period: String) : DashboardIntent()
    data class HandleAction(val actionId: String) : DashboardIntent()
    object ClearError : DashboardIntent()
}

## DashboardEffect
sealed class DashboardEffect : UiEffect {
    object NavigateToPayments : DashboardEffect()
    object NavigateToAccounts : DashboardEffect()
    data class ShowToast(val message: String) : DashboardEffect()
    data class ShowError(val message: String) : DashboardEffect()
}

## Reducer Logic
LoadScreen              → GetDashboardUseCase + GetChartDataUseCase(selectedPeriod) in parallel
                          → setState(dashboardData, chartData, screenModel)
RefreshDashboard        → isLoading=true → same as LoadScreen
ToggleNetWorthVisibility → setState(isNetWorthVisible = !isNetWorthVisible)
ChangePeriod(period)    → setState(selectedPeriod=period) → GetChartDataUseCase(period)
HandleAction("TRANSFER")         → setEffect(NavigateToPayments)
HandleAction("PAY_BILLS")        → setEffect(ShowToast("Not implemented yet"))
HandleAction("MANAGE_ACCOUNTS")  → setEffect(NavigateToAccounts)
HandleAction("VIEW_ALL_ACTIVITY")→ setEffect(ShowToast("Not implemented yet"))
HandleAction("LEARN_MORE")       → setEffect(ShowToast("Not implemented yet"))
HandleAction("TOGGLE_NET_WORTH") → dispatch ToggleNetWorthVisibility
ClearError              → setState(error = null)

## MainState (bottom nav host)
data class MainState(
    val selectedTab: BottomTab = BottomTab.HOME,
    val tabs: List<BottomNavTab> = emptyList()  // loaded from bottom_nav_config.json
) : UiState

sealed class MainIntent : UiIntent {
    data class SelectTab(val tab: BottomTab) : MainIntent()
    object LoadNavConfig : MainIntent()
}

sealed class MainEffect : UiEffect // reserved

enum class BottomTab { HOME, PAYMENTS, ACCOUNTS, PROFILE }

data class BottomNavTab(
    val id: String,
    val label: String,
    val icon: String,
    val route: String
)
