package com.architect.banking.feature.profile

import com.architect.banking.core.domain.UiEffect
import com.architect.banking.core.domain.UiIntent
import com.architect.banking.core.domain.UiState
import com.architect.banking.engine.navigation.NavigationAction
import com.architect.banking.engine.sdui.model.ScreenModel

/**
 * UI state for the Profile screen.
 *
 * @property screenModel SDUI screen definition. Null while loading.
 * @property isLoading True during API calls.
 * @property error Inline error message, or null when clean.
 */
data class ProfileState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
) : UiState

/** User-initiated actions on the Profile screen. */
sealed class ProfileIntent : UiIntent {

    /** Triggers loading of the profile screen SDUI JSON. */
    object LoadScreen : ProfileIntent()

    /**
     * Handles any action dispatched by the SDUI renderer.
     * @property actionId The action key from the screen's "actions" map.
     */
    data class HandleAction(val actionId: String) : ProfileIntent()
}

/** One-shot effects for the Profile screen. */
sealed class ProfileEffect : UiEffect {

    /**
     * Triggers navigation via [NavigationEngine].
     * @property action The resolved navigation action.
     */
    data class Navigate(val action: NavigationAction) : ProfileEffect()

    /**
     * Displays an error or confirmation dialog.
     * @property message Human-readable message text.
     */
    data class ShowDialog(val message: String) : ProfileEffect()
}
