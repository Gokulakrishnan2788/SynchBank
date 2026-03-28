package com.architect.banking.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.feature.dashboard.DashboardScreen
import com.architect.banking.feature.payments.AddBeneficiaryScreen
import com.architect.banking.feature.payments.TransferScreen
import com.architect.banking.feature.profile.ProfileScreen

private const val TAB_HOME = "tab_home"
private const val TAB_ACCOUNTS = "tab_accounts"
private const val TAB_PAYMENTS = "tab_payments"
private const val TAB_PROFILE = "tab_profile"

private data class BottomTab(
    val route: String,
    val label: String,
    val icon: ImageVector,
)

private val bottomTabs = listOf(
    BottomTab(TAB_HOME, "Home", Icons.Default.Home),
    BottomTab(TAB_PAYMENTS, "Payments", Icons.Default.Payment),
    BottomTab(TAB_ACCOUNTS, "Accounts", Icons.Default.AccountBalance),
    BottomTab(TAB_PROFILE, "Profile", Icons.Default.Person),
)

/**
 * Main shell screen that hosts bottom navigation.
 *
 * Uses a fully custom bottom nav bar so the selected tab shows a rounded-rect
 * pill behind both the icon AND the label — matching the design spec.
 *
 * @param rootNavController The application-level nav controller (from [ArchitectNavHost]).
 */
@Composable
fun MainScreen(rootNavController: NavController) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ArchitectColors.White)
                    .navigationBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                bottomTabs.forEach { tab ->
                    val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (selected) ArchitectColors.NavyPrimary else Color.Transparent)
                            .clickable {
                                bottomNavController.navigate(tab.route) {
                                    popUpTo(bottomNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = tab.label,
                            tint = if (selected) ArchitectColors.White else ArchitectColors.MediumGray,
                        )
                        Text(
                            text = tab.label,
                            fontSize = 11.sp,
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (selected) ArchitectColors.White else ArchitectColors.MediumGray,
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = TAB_HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(TAB_HOME) {
                DashboardScreen(
                    navController = rootNavController,
                    onSwitchTab = { route ->
                        bottomNavController.navigate(route) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
            composable(TAB_PAYMENTS) {
                val paymentsNavController = rememberNavController()
                NavHost(
                    navController = paymentsNavController,
                    startDestination = "transfer",
                ) {
                    composable("transfer") {
                        TransferScreen(navController = paymentsNavController)
                    }
                    composable("add_beneficiary") {
                        AddBeneficiaryScreen(navController = paymentsNavController)
                    }
                }
            }
            composable(TAB_ACCOUNTS) {
                AccountsPlaceholder()
            }
            composable(TAB_PROFILE) {
                ProfileScreen(navController = rootNavController)
            }
        }
    }
}

@Composable
private fun AccountsPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Accounts", color = ArchitectColors.MediumGray)
    }
}

