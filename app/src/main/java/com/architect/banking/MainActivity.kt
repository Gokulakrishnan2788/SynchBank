package com.architect.banking

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.architect.banking.core.ui.theme.ArchitectTheme
import com.architect.banking.navigation.ArchitectNavHost
import dagger.hilt.android.AndroidEntryPoint

/**
 * Single activity — entry point for the entire app.
 *
 * [AndroidEntryPoint] enables Hilt injection on this activity and all
 * composables in its hierarchy (via [hiltViewModel]).
 *
 * The activity only sets up [ArchitectTheme] and [ArchitectNavHost];
 * all feature logic lives in the respective feature modules.
 */
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ArchitectTheme {
                val navController = rememberNavController()
                ArchitectNavHost(navController = navController)
            }
        }
    }
}
