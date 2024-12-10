package com.holparb.moviefinder.movies.presentation.main_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.holparb.moviefinder.core.navigation.Destination
import com.holparb.moviefinder.core.navigation.Navigation
import com.holparb.moviefinder.movies.presentation.main_screen.components.BottomNavigationBar

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    var bottomNavVisible by rememberSaveable {
        mutableStateOf(true)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    navBackStackEntry?.destination?.let { currentDestination ->
        bottomNavVisible = (currentDestination.hasRoute(Destination.HomeScreenDestination::class)
                || currentDestination.hasRoute(Destination.WatchlistDestination::class)
                || currentDestination.hasRoute(Destination.SearchScreenDestination::class))
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        bottomBar = {
            if(bottomNavVisible) {
                BottomNavigationBar(
                    navController = navController
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if(bottomNavVisible) paddingValues.calculateBottomPadding() else 0.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Navigation(navController = navController)
        }
    }

}
