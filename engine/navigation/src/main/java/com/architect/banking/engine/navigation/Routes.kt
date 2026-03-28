package com.architect.banking.engine.navigation

/**
 * Centralized route constants for all screens.
 * All routes match the "destination" values in navigation action JSON.
 *
 * Never pass route strings directly to [NavController.navigate] —
 * always use [NavigationEngine].
 */
object Routes {
    const val LOGIN = "login"
    const val MAIN = "main"
    const val DASHBOARD = "dashboard"
    const val PROFILE = "profile"
    const val ACCOUNTS = "accounts"
    const val ACCOUNT_DETAIL = "accounts/{accountId}"
    const val TRANSFER = "transfer"
    const val TRANSACTIONS = "transactions"
    const val FORGOT_PASSWORD = "forgot_password"

    /** Builds the resolved account detail route for a specific [accountId]. */
    fun accountDetail(accountId: String) = "accounts/$accountId"
}
