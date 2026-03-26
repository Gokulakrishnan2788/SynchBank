package com.architect.banking.feature.profile

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
 * MVI ViewModel for the Profile screen.
 *
 * Loads the SDUI screen definition and handles all action dispatches
 * including logout.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val screenApiService: ScreenApiService,
) : BaseViewModel<ProfileState, ProfileIntent, ProfileEffect>() {

    override fun initialState() = ProfileState()

    init {
        handleIntent(ProfileIntent.LoadScreen)
    }

    override suspend fun reduce(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.LoadScreen -> loadScreen()
            is ProfileIntent.HandleAction -> onHandleAction(intent.actionId)
        }
    }

//    private suspend fun loadScreen() {
//        setState { copy(isLoading = true, error = null) }
//        when (val result = safeApiCall { screenApiService.getScreen(Routes.PROFILE) }) {
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

    private suspend fun executeAction(action: ActionModel) {
        when (action.type) {
            SduiActionType.NAVIGATE -> {
                val destination = action.destination ?: return
                setEffect(ProfileEffect.Navigate(NavigationAction(destination = destination)))
            }
            SduiActionType.API_CALL -> handleApiCall(action)
            SduiActionType.SHOW_DIALOG -> {
                val message = action.params["message"] ?: "An error occurred"
                setEffect(ProfileEffect.ShowDialog(message))
            }
            SduiActionType.DEEP_LINK -> {
                val deepLink = action.destination ?: return
                setEffect(
                    ProfileEffect.Navigate(
                        NavigationAction(type = NavigationType.DEEP_LINK, deepLink = deepLink),
                    ),
                )
            }
            else -> Unit
        }
    }

    private suspend fun handleApiCall(action: ActionModel) {
        setState { copy(isLoading = true) }
        // Phase 1 stub: simulate API call. Phase 2 wires real logout use case.
        kotlinx.coroutines.delay(500)
        setState { copy(isLoading = false) }
        action.onSuccess?.let { executeAction(it) }
    }
}
