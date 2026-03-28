package com.architect.banking.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.feature.dashboard.DashboardScreen
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
    BottomTab(TAB_ACCOUNTS, "Accounts", Icons.Default.AccountBalance),
    BottomTab(TAB_PAYMENTS, "Payments", Icons.Default.Payment),
    BottomTab(TAB_PROFILE, "Profile", Icons.Default.Person),
)

/**
 * Main shell screen that hosts bottom navigation.
 *
 * Owns a nested [NavHost] for tab content. Receives [rootNavController] so
 * individual tab screens can trigger global navigation (e.g. deep links).
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
            NavigationBar(
                containerColor = ArchitectColors.White,
            ) {
                bottomTabs.forEach { tab ->
                    val selected = currentDestination?.hierarchy?.any { it.route == tab.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            bottomNavController.navigate(tab.route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(imageVector = tab.icon, contentDescription = tab.label)
                        },
                        label = { Text(text = tab.label) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = ArchitectColors.White,
                            selectedTextColor = ArchitectColors.NavyPrimary,
                            unselectedIconColor = ArchitectColors.MediumGray,
                            unselectedTextColor = ArchitectColors.MediumGray,
                            indicatorColor = ArchitectColors.NavyPrimary,
                        ),
                    )
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
            composable(TAB_ACCOUNTS) {
                AccountsPlaceholder()
            }
            composable(TAB_PAYMENTS) {
                PaymentsPlaceholder()
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

@Composable
private fun PaymentsPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Payments", color = ArchitectColors.MediumGray)
    }
}
