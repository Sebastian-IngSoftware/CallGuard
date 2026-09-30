package com.callguard.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.callguard.app.ui.blacklist.BlacklistScreen
import com.callguard.app.ui.home.HomeScreen
import com.callguard.app.ui.settings.SettingsScreen

object Routes {
    const val HOME = "home"
    const val BLACKLIST = "blacklist"
    const val SETTINGS = "settings"
}

@Composable
fun CallGuardNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToBlacklist = { navController.navigate(Routes.BLACKLIST) },
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }
        composable(Routes.BLACKLIST) { BlacklistScreen() }
        composable(Routes.SETTINGS) { SettingsScreen() }
    }
}
