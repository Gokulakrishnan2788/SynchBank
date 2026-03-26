package com.architect.banking.feature.dashboard

import com.architect.banking.core.domain.UiEffect
import com.architect.banking.core.domain.UiIntent
import com.architect.banking.core.domain.UiState
import com.architect.banking.engine.navigation.NavigationAction
import com.architect.banking.engine.sdui.model.ScreenModel

/**
 * UI state for the Dashboard screen.
 *
 * @property screenModel SDUI screen definition. Null while loading.
 * @property isLoading True during API calls.
 * @property error Inline error message, or null when clean.
 */
data class DashboardState(
    val screenModel: ScreenModel? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
) : UiState

/** User-initiated actions on the Dashboard screen. */
sealed class DashboardIntent : UiIntent {

    /** Triggers loading of the dashboard screen SDUI JSON. */
    object LoadScreen : DashboardIntent()

    /**
     * Handles any action dispatched by the SDUI renderer.
     * @property actionId The action key from the screen's "actions" map.
     */
    data class HandleAction(val actionId: String) : DashboardIntent()
}

/** One-shot effects for the Dashboard screen. */
sealed class DashboardEffect : UiEffect {

    /**
     * Triggers navigation via [NavigationEngine].
     * @property action The resolved navigation action.
     */
    data class Navigate(val action: NavigationAction) : DashboardEffect()

    /**
     * Displays an error dialog.
     * @property message Human-readable error text.
     */
    data class ShowDialog(val message: String) : DashboardEffect()
}
