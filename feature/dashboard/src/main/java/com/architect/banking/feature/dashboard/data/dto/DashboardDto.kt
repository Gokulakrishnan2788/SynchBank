package com.architect.banking.feature.dashboard.data.dto

import kotlinx.serialization.Serializable

/**
 * Response payload for a successful `GET /dashboard` call.
 *
 * @property netWorth Total net worth across all accounts (e.g., 142850.42).
 * @property netWorthChange Formatted percentage change (e.g., "+2.4%").
 * @property netWorthChangePositive True if net worth increased, false if decreased.
 * @property accounts List of account summaries to display.
 * @property activity List of recent transaction activity items.
 * @property exclusiveOffer Optional promotional offer card data.
 */
@Serializable
data class DashboardResponseDto(
    val netWorth: Double,
    val netWorthChange: String,
    val netWorthChangePositive: Boolean,
    val accounts: List<AccountSummaryDto>,
    val activity: List<ActivityItemDto>,
    val exclusiveOffer: ExclusiveOfferDto? = null,
)

/**
 * Summary information for a single account displayed on the dashboard.
 *
 * @property id Unique account identifier.
 * @property type Account type display name (e.g., "ELITE CHECKING").
 * @property balance Formatted balance string (e.g., "$42,301.15").
 * @property detail Additional account detail (e.g., "Available • ••• 9012").
 * @property iconAsset Asset name for the account icon (e.g., "ic_account_checking").
 */
@Serializable
data class AccountSummaryDto(
    val id: String,
    val type: String,
    val balance: String,
    val detail: String,
    val iconAsset: String,
)

/**
 * A single activity item representing a transaction or account event.
 *
 * @property id Unique activity identifier.
 * @property title Primary display text (e.g., "Apple Store").
 * @property subtitle Secondary display text (e.g., "Electronics • Today").
 * @property amount Formatted amount string (e.g., "-$1,299.00").
 * @property amountPositive True for credits/deposits, false for debits/withdrawals.
 * @property iconAsset Asset name for the activity icon (e.g., "ic_txn_shopping").
 * @property iconBgColor Background color token for the icon (e.g., "IconBgGrey").
 */
@Serializable
data class ActivityItemDto(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String,
    val amountPositive: Boolean,
    val iconAsset: String,
    val iconBgColor: String,
)

/**
 * Promotional offer card data displayed on the dashboard.
 *
 * @property badgeLabel Small label displayed above the title (e.g., "EXCLUSIVE OFFER").
 * @property title Main offer description.
 * @property ctaLabel Call-to-action button text (e.g., "Learn more →").
 * @property bgColor Background color token (e.g., "NavyPrimary").
 * @property illustrationAsset Asset name for the offer illustration.
 */
@Serializable
data class ExclusiveOfferDto(
    val badgeLabel: String,
    val title: String,
    val ctaLabel: String,
    val bgColor: String,
    val illustrationAsset: String,
)

/**
 * Response payload for a successful `GET /dashboard/chart` call.
 *
 * @property period Human-readable period description (e.g., "Last 6 Months").
 * @property dataPoints Array of net worth values over time, ordered chronologically.
 */
@Serializable
data class ChartDataDto(
    val period: String,
    val dataPoints: List<Double>,
)
