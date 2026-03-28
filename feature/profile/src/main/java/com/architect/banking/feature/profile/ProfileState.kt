package com.architect.banking.feature.profile

import com.architect.banking.core.domain.UiEffect
import com.architect.banking.core.domain.UiIntent
import com.architect.banking.core.domain.UiState
import com.architect.banking.engine.navigation.NavigationAction
import com.architect.banking.engine.sdui.model.ScreenModel

data class ProfileState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val profileImageUri: String? = null,
) : UiState

sealed class ProfileIntent : UiIntent {
    object LoadScreen : ProfileIntent()
    data class HandleAction(val actionId: String) : ProfileIntent()
    data class UpdateProfileImage(val uri: String) : ProfileIntent()
}

sealed class ProfileEffect : UiEffect {
    data class Navigate(val action: NavigationAction) : ProfileEffect()
    data class ShowDialog(val message: String) : ProfileEffect()
    object LaunchLanguageSettings : ProfileEffect()
    object LaunchDisplaySettings : ProfileEffect()
}
