package com.architect.banking.engine.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

/**
 * The single class allowed to call [NavController.navigate].
 * All navigation is driven by [NavigationAction] data — no feature calls
 * [NavController.navigate] directly.
 *
 * Usage in NavHost:
 * ```
 * LaunchedEffect(Unit) {
 *     viewModel.effect.collect { effect ->
 *         when (effect) {
 *             is XEffect.Navigate -> navigationEngine.navigate(navController, effect.action)
 *         }
 *     }
 * }
 * ```
 */
object NavigationEngine {

    /**
     * Executes [action] against [navController].
     *
     * @param navController The NavController from the NavHost.
     * @param action The navigation instruction to execute.
     */
    fun navigate(navController: NavController, action: NavigationAction) {
        when (action.type) {
            NavigationType.PUSH -> {
                navController.navigate(action.destination, buildNavOptions(action))
            }
            NavigationType.REPLACE -> {
                val options = NavOptions.Builder()
                    .setPopUpTo(
                        navController.currentDestination?.route ?: action.destination,
                        inclusive = true,
                    )
                    .build()
                navController.navigate(action.destination, options)
            }
            NavigationType.POP -> {
                if (action.destination.isEmpty()) {
                    navController.popBackStack()
                } else {
                    navController.popBackStack(
                        route = action.destination,
                        inclusive = action.inclusive,
                    )
                }
            }
            NavigationType.DEEP_LINK -> {
                val uri = action.deepLink ?: return
                val request = androidx.navigation.NavDeepLinkRequest.Builder
                    .fromUri(android.net.Uri.parse(uri))
                    .build()
                navController.navigate(request)
            }
            NavigationType.MODAL -> {
                navController.navigate(action.destination, buildNavOptions(action))
            }
        }
    }

    private fun buildNavOptions(action: NavigationAction): NavOptions? {
        val popUpTo = action.popUpTo ?: return null
        return NavOptions.Builder()
            .setPopUpTo(popUpTo, inclusive = action.inclusive)
            .build()
    }
}
