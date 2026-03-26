package com.architect.banking.feature.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.architect.banking.engine.navigation.Routes

/**
 * Adds the Profile feature's composable destinations to the [NavGraphBuilder].
 *
 * @param navController Passed through to [ProfileScreen] for effect-driven navigation.
 */
fun NavGraphBuilder.profileGraph(navController: NavController) {
    composable(route = Routes.PROFILE) {
        ProfileScreen(navController = navController)
    }
}
