package com.logan.digitallibrary.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AllInbox
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.logan.digitallibrary.ui.screens.library.LibraryScreen
import com.logan.digitallibrary.ui.screens.explore.ExploreScreen
import com.logan.digitallibrary.ui.screens.settings.SettingsScreen
import com.logan.digitallibrary.ui.reader.ReaderScreen
import kotlinx.coroutines.flow.map

sealed class Screen(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Library : Screen(
        route = "library",
        title = "Библиотека",
        selectedIcon = Icons.Filled.Book,
        unselectedIcon = Icons.Outlined.Book
    )
    
    object Explore : Screen(
        route = "explore",
        title = "Импорт",
        selectedIcon = Icons.Filled.AllInbox,
        unselectedIcon = Icons.Outlined.AllInbox
    )

    
    object Settings : Screen(
        route = "settings",
        title = "Настройки",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
    
    object Reader : Screen(
        route = "reader/{bookId}",
        title = "Чтение",
        selectedIcon = Icons.Filled.Create,
        unselectedIcon = Icons.Outlined.Create
    ) {
        fun createRoute(bookId: String) = "reader/$bookId"
    }

    companion object {
        val bottomNavItems = listOf(Library, Explore, Settings)
    }
}

@Composable
fun digitallibraryNavGraph(
    navController: NavHostController,
    onShowBottomBar: (Boolean) -> Unit
) {
    // Hide bottom bar when entering reader screen, show it when leaving
    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.map { entry ->
            entry.destination.route?.startsWith("reader") != true
        }.collect { showBottomBar ->
            onShowBottomBar(showBottomBar)
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Library.route
    ) {
        composable(Screen.Library.route) {
            LibraryScreen(
                onBookClick = { bookId ->
                    navController.navigate(Screen.Reader.createRoute(bookId))
                }
            )
        }
        
        composable(Screen.Explore.route) {
            ExploreScreen()
        }



        composable(Screen.Settings.route) {
            SettingsScreen()
        }
        
        composable(
            route = Screen.Reader.route
        ) { backStackEntry ->
            val bookId = checkNotNull(backStackEntry.arguments?.getString("bookId")) {
                "bookId parameter wasn't found. Please make sure it's passed."
            }
            ReaderScreen(
                bookId = bookId,
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
