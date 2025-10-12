package com.spundev.ringvolume.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.spundev.ringvolume.ui.screens.auto.AutoScreen
import com.spundev.ringvolume.ui.screens.home.HomeRoute
import kotlinx.serialization.Serializable

@Serializable
private object Home : NavKey

@Serializable
private object Auto : NavKey

private fun NavBackStack<NavKey>.navigateToAuto() {
    this.add(Auto)
}

@Composable
fun AppNavHost(windowSizeClass: WindowSizeClass) {
    val backStack = rememberNavBackStack(Home)
    NavDisplay(
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSaveableStateHolderNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        entryProvider = entryProvider {
            entry<Home> {
                HomeRoute(
                    onNavigateToAuto = backStack::navigateToAuto,
                    windowSizeClass = windowSizeClass
                )
            }
            entry<Auto> {
                AutoScreen()
            }
        }
    )
}