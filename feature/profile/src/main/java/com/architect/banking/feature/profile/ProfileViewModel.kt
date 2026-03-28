package com.architect.banking.feature.profile

import android.content.Context
import com.architect.banking.core.domain.BaseViewModel
import com.architect.banking.core.domain.Result
import com.architect.banking.engine.navigation.NavigationAction
import com.architect.banking.engine.navigation.NavigationType
import com.architect.banking.engine.sdui.model.ActionModel
import com.architect.banking.engine.sdui.model.ScreenModel
import com.architect.banking.engine.sdui.model.SduiActionType
import com.architect.banking.engine.sdui.model.SduiComponentType
import com.architect.banking.engine.sdui.parser.SDUIParser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val sduiParser: SDUIParser,
    @ApplicationContext private val context: Context,
) : BaseViewModel<ProfileState, ProfileIntent, ProfileEffect>() {

    override fun initialState() = ProfileState()

    init {
        handleIntent(ProfileIntent.LoadScreen)
    }

    override suspend fun reduce(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.LoadScreen -> loadScreen()
            is ProfileIntent.HandleAction -> onHandleAction(intent.actionId)
            is ProfileIntent.UpdateProfileImage -> {
                setState { copy(profileImageUri = intent.uri) }
                val model = state.value.screenModel ?: return
                val patched = model.withPatchedAvatarImage(intent.uri)
                setState { copy(screenModel = patched) }
            }
        }
    }

    private suspend fun loadScreen() {
        setState { copy(isLoading = true, error = null) }
        try {
            val json = context.assets
                .open("mock/screens/profile_screen.json")
                .bufferedReader()
                .use { it.readText() }
            when (val result = sduiParser.parse(json)) {
                is Result.Success -> setState { copy(isLoading = false, screenModel = result.data) }
                is Result.Error -> setState { copy(isLoading = false, error = result.message) }
                is Result.Loading -> Unit
            }
        } catch (e: Exception) {
            setState { copy(isLoading = false, error = "Failed to load screen: ${e.message}") }
        }
    }

    private suspend fun onHandleAction(actionId: String) {
        if (actionId == "EDIT_PROFILE") return // handled in ProfileScreen
        if (actionId == "APPEARANCE") {
            setEffect(ProfileEffect.LaunchDisplaySettings)
            return
        }
        if (actionId == "LANGUAGE") {
            setEffect(ProfileEffect.LaunchLanguageSettings)
            return
        }
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

    private fun ScreenModel.withPatchedAvatarImage(imageUri: String): ScreenModel {
        return copy(
            components = components.map { comp ->
                if (comp.type == SduiComponentType.PROFILE_AVATAR_HEADER) {
                    val newProps = buildJsonObject {
                        comp.props.forEach { (key, value) -> put(key, value) }
                        put("imagePath", imageUri)
                    }
                    comp.copy(props = newProps)
                } else comp
            }
        )
    }
}
