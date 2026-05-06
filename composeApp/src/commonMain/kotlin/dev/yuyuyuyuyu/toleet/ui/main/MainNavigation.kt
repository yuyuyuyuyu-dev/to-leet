package dev.yuyuyuyuyu.toleet.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import dev.yuyuyuyuyu.toleet.ui.openSourceLicenses.OpenSourceLicensesScreen
import dev.yuyuyuyuyu.toleet.ui.toLeet.ToLeetScreen
import dev.yuyuyuyuyu.toleet.ui.toLeet.ToLeetViewModelImpl

@Composable
fun MainNavigation(
    backStack: MutableList<MainNavigationRoute>,
    modifier: Modifier = Modifier,
) {
    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                MainNavigationRoute.HowOldAmI -> {
                    NavEntry(key) {
                        ToLeetScreen(ToLeetViewModelImpl())
                    }
                }

                MainNavigationRoute.OpenSourceLicenses -> {
                    NavEntry(key) {
                        OpenSourceLicensesScreen()
                    }
                }
            }
        },
    )
}
