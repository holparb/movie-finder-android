package com.holparb.moviefinder.movies.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.holparb.moviefinder.ui.theme.SecondaryTextColor

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    selectedIndex: Int,
    changeNavigationSelectedIndex: (Int) -> Unit,
) {
    val bottomNavItems = listOf(
        BottomNavigationItem(
            title = "Search",
            selectedIcon = Icons.Filled.Search,
            unselectedIcon = Icons.Outlined.Search
        ),
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Watchlist",
            selectedIcon = Icons.Filled.Favorite,
            unselectedIcon = Icons.Outlined.Favorite
        )
    )
    NavigationBar(
        tonalElevation = 0.dp
    ) {
        bottomNavItems.forEachIndexed { index, bottomNavigationItem ->
            val selected = selectedIndex == index
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor =  MaterialTheme.colorScheme.surface
                ),
                selected = selected,
                onClick = {
                    changeNavigationSelectedIndex(index)
                },
                icon = {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        imageVector = if (selected) bottomNavigationItem.selectedIcon else bottomNavigationItem.unselectedIcon,
                        contentDescription = "Some icon",
                        tint = if(selected) MaterialTheme.colorScheme.primary else SecondaryTextColor
                    )
                },
                label = {
                    Text(
                        text = bottomNavigationItem.title,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if(selected) MaterialTheme.colorScheme.primary else SecondaryTextColor
                        )
                    )
                }
            )
        }
    }
}