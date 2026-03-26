# Dynamic Navigation Contract
# ALL navigation must be API-driven. No hardcoded navController.navigate("string") allowed.

## NavigationEngine (in :engine:navigation — DO NOT regenerate)
Consumes NavigationAction from SDUI action handler
Translates to NavController commands
Handles deep links via intent parsing

## Route Registry (in :engine:navigation)
object Routes {
    const val LOGIN = "login"
    const val DASHBOARD = "dashboard"
    const val ACCOUNTS = "accounts"
    const val ACCOUNT_DETAIL = "accounts/{accountId}"
    const val TRANSFER = "transfer"
    const val TRANSACTIONS = "transactions"
}

## NavGraph Pattern Per Feature
// In :feature:login
fun NavGraphBuilder.loginGraph(
    navController: NavController,
    navigationEngine: NavigationEngine
) {
    composable(Routes.LOGIN) {
        LoginScreen(navController = navController, navigationEngine = navigationEngine)
    }
}

// In :app — NavHost wires everything
@Composable
fun ArchitectNavHost(navController: NavHostController) {
    val navigationEngine = hiltViewModel<NavigationViewModel>()
    NavHost(navController, startDestination = Routes.LOGIN) {
        loginGraph(navController, navigationEngine)
        dashboardGraph(navController, navigationEngine)
        // ...
    }
}

## Navigation Config JSON (assets/mock/nav/navigation_config.json)
{
  "version": "1.0",
  "flows": {
    "auth_flow": {
      "start": "login",
      "transitions": {
        "login": { "onSuccess": "dashboard", "onForgot": "forgot_password" },
        "forgot_password": { "onSubmit": "login" }
      }
    },
    "main_flow": {
      "start": "dashboard",
      "transitions": {
        "dashboard": { "onAccounts": "accounts", "onTransfer": "transfer" },
        "accounts": { "onSelect": "accounts/{accountId}" },
        "transfer": { "onSuccess": "dashboard" }
      }
    }
  },
  "deepLinks": {
    "architect://dashboard": "dashboard",
    "architect://accounts/{id}": "accounts/{id}",
    "architect://transfer": "transfer"
  }
}

## Rules
- NavigationEngine must be the ONLY class calling navController.navigate()
- Features emit NavigationAction via Effect — never call navController directly
- All routes must have a corresponding deep link defined in navigation_config.json
- Back stack management (POP, REPLACE) handled by NavigationEngine only
