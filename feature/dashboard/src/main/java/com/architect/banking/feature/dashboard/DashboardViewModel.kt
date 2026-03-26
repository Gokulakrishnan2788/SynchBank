package com.architect.banking.feature.dashboard

import com.architect.banking.core.domain.BaseViewModel
import com.architect.banking.core.domain.Result
import com.architect.banking.core.network.model.safeApiCall
import com.architect.banking.engine.navigation.NavigationAction
import com.architect.banking.engine.navigation.NavigationType
import com.architect.banking.engine.navigation.Routes
import com.architect.banking.engine.sdui.api.ScreenApiService
import com.architect.banking.engine.sdui.model.ActionModel
import com.architect.banking.engine.sdui.model.SduiActionType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * MVI ViewModel for the Dashboard screen.
 *
 * Loads the SDUI screen definition and handles all action dispatches
 * from the SDUI renderer.
 */
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val screenApiService: ScreenApiService,
) : BaseViewModel<DashboardState, DashboardIntent, DashboardEffect>() {

    override fun initialState() = DashboardState()

    init {
        handleIntent(DashboardIntent.LoadScreen)
    }

    override suspend fun reduce(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.LoadScreen -> loadScreen()
            is DashboardIntent.HandleAction -> onHandleAction(intent.actionId)
        }
    }

//    private suspend fun loadScreen() {
//        setState { copy(isLoading = true, error = null) }
//        when (val result = safeApiCall { screenApiService.getScreen(Routes.DASHBOARD) }) {
//            is Result.Success -> setState { copy(isLoading = false, screenModel = result.data) }
//            is Result.Error -> setState { copy(isLoading = false, error = result.message) }
//            is Result.Loading -> Unit
//        }
//    }
private suspend fun loadScreen() {
    setState { copy(isLoading = true, error = null) }
    setState { copy(isLoading = false) }
}

    private suspend fun onHandleAction(actionId: String) {
        val action = state.value.screenModel?.actions?.get(actionId) ?: return
        executeAction(action)
    }

    private fun executeAction(action: ActionModel) {
        when (action.type) {
            SduiActionType.NAVIGATE -> {
                val destination = action.destination ?: return
                setEffect(DashboardEffect.Navigate(NavigationAction(destination = destination)))
            }
            SduiActionType.SHOW_DIALOG -> {
                val message = action.params["message"] ?: "An error occurred"
                setEffect(DashboardEffect.ShowDialog(message))
            }
            SduiActionType.DEEP_LINK -> {
                val deepLink = action.destination ?: return
                setEffect(
                    DashboardEffect.Navigate(
                        NavigationAction(type = NavigationType.DEEP_LINK, deepLink = deepLink),
                    ),
                )
            }
            else -> Unit
        }
    }
}
