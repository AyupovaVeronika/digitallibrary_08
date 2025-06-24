package com.logan.digitallibrary.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AllInbox
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Library : BottomNavItem(
        route = "library",
        title = "Библиотека",
        selectedIcon = Icons.Filled.Book,
        unselectedIcon = Icons.Outlined.Book
    )

    object Explore : BottomNavItem(
        route = "explore",
        title = "Импорт",
        selectedIcon = Icons.Filled.AllInbox,
        unselectedIcon = Icons.Outlined.AllInbox
    )


    object Settings : BottomNavItem(
        route = "settings",
        title = "Настройки",
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )

    companion object {
        val items = listOf(Library ,Explore, Settings)
    }
}
