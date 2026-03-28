package com.architect.banking.feature.payments

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

private const val ROUTE_TRANSFER = "transfer"
private const val ROUTE_ADD_BENEFICIARY = "add_beneficiary"

/**
 * Adds the Payments feature composable destinations to the [NavGraphBuilder].
 *
 * Registers:
 * - "transfer" — the main Transfer/Payments screen
 * - "add_beneficiary" — the Add Beneficiary form screen
 *
 * @param navController Passed through to screens for effect-driven navigation.
 */
fun NavGraphBuilder.paymentsGraph(navController: NavController) {
    composable(route = ROUTE_TRANSFER) {
        TransferScreen(navController = navController)
    }
    composable(route = ROUTE_ADD_BENEFICIARY) {
        AddBeneficiaryScreen(navController = navController)
    }
}
