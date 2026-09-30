package com.callguard.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.callguard.app.ui.blacklist.BlacklistScreen
import com.callguard.app.ui.home.HomeScreen
import com.callguard.app.ui.news.NewsScreen
import com.callguard.app.ui.settings.SettingsScreen

object Routes {
    const val HOME = "home"
    const val BLACKLIST = "blacklist"
    const val SETTINGS = "settings"
    const val NEWS = "news"
}

@Composable
fun CallGuardNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToBlacklist = { navController.navigate(Routes.BLACKLIST) },
                onNavigateToSettings = { navController.navigate(Routes.SETTINGS) },
                onNavigateToNews = { navController.navigate(Routes.NEWS) }
            )
        }
        composable(Routes.BLACKLIST) { BlacklistScreen() }
        composable(Routes.SETTINGS) { SettingsScreen() }
        composable(Routes.NEWS) { NewsScreen() }
    }
}
