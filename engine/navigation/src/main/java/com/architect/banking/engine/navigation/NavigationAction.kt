package com.architect.banking.engine.navigation

import kotlinx.serialization.Serializable

/**
 * Navigation action types supported by [NavigationEngine].
 */
enum class NavigationType {
    /** Push a new destination onto the back stack. */
    PUSH,

    /** Replace the current destination (no back entry). */
    REPLACE,

    /** Pop the back stack to the previous destination. */
    POP,

    /** Navigate via a deep link URI. */
    DEEP_LINK,

    /** Open destination as a modal / bottom sheet. */
    MODAL,
}

/**
 * Data class describing a navigation operation to be executed by [NavigationEngine].
 * Produced by ViewModels as a [UiEffect] and consumed in the NavHost.
 *
 * @property type The kind of navigation to perform.
 * @property destination Route string matching a [Routes] constant.
 * @property params Optional query/path params to pass to the destination.
 * @property deepLink URI for [NavigationType.DEEP_LINK] navigations.
 * @property popUpTo Optional route to pop up to before navigating.
 * @property inclusive Whether [popUpTo] itself is also removed from the stack.
 */
@Serializable
data class NavigationAction(
    val type: NavigationType = NavigationType.PUSH,
    val destination: String = "",
    val params: Map<String, String> = emptyMap(),
    val deepLink: String? = null,
    val popUpTo: String? = null,
    val inclusive: Boolean = false,
)
