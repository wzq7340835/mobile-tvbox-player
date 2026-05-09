package com.fongmi.android.tv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fongmi.android.tv.ui.screen.*

object Routes {
    const val HOME = "home"
    const val VOD = "vod/{siteKey}/{vodId}"
    const val SEARCH = "search"
    const val LIVE = "live"
    const val PLAYER = "player/{url}"
    const val SETTING = "setting"
    const val KEEP = "keep"
    const val HISTORY = "history"
    const val CONFIG = "config"
}

@Composable
fun TVNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable(Routes.HOME) {
            HomeScreen(
                onVodClick = { siteKey, vodId ->
                    navController.navigate("vod/$siteKey/$vodId")
                },
                onSearchClick = { navController.navigate(Routes.SEARCH) },
                onLiveClick = { navController.navigate(Routes.LIVE) },
                onSettingClick = { navController.navigate(Routes.SETTING) },
                onKeepClick = { navController.navigate(Routes.KEEP) },
                onHistoryClick = { navController.navigate(Routes.HISTORY) }
            )
        }
        composable(Routes.SEARCH) {
            SearchScreen(
                onBack = { navController.popBackStack() },
                onVodClick = { siteKey, vodId ->
                    navController.navigate("vod/$siteKey/$vodId")
                }
            )
        }
        composable(Routes.LIVE) {
            LiveScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.SETTING) {
            SettingScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.KEEP) {
            KeepScreen(
                onBack = { navController.popBackStack() },
                onVodClick = { siteKey, vodId ->
                    navController.navigate("vod/$siteKey/$vodId")
                }
            )
        }
        composable(Routes.HISTORY) {
            HistoryScreen(
                onBack = { navController.popBackStack() },
                onVodClick = { siteKey, vodId ->
                    navController.navigate("vod/$siteKey/$vodId")
                }
            )
        }
    }
}
