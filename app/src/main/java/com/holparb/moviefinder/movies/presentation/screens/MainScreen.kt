package com.holparb.moviefinder.movies.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.holparb.moviefinder.movies.presentation.components.Navigation
import com.holparb.moviefinder.movies.presentation.viewmodels.MainViewModel

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@Composable
fun MainScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val state by mainViewModel.state.collectAsStateWithLifecycle()
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
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        color = Color.White//MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    contentColor = Color.White
                ) {
                    bottomNavItems.forEachIndexed { index, bottomNavigationItem ->
                        NavigationBarItem(selected = state.selectedIndex == index,
                            onClick = {
                                mainViewModel.changeNavigationSelectedIndex(index)
                            },
                            icon = {
                                if(state.selectedIndex == index) bottomNavigationItem.selectedIcon else bottomNavigationItem.unselectedIcon
                            })
                    }
                }
            }
        ) { paddingValues ->
            Navigation(navController = navController, paddingValues = paddingValues)
        }
    }
}