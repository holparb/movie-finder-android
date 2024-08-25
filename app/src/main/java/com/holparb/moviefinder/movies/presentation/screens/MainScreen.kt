package com.holparb.moviefinder.movies.presentation.screens

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.holparb.moviefinder.movies.presentation.components.BottomNavigationBar
import com.holparb.moviefinder.movies.presentation.components.navigation.BottomNavScreenComposable
import com.holparb.moviefinder.movies.presentation.components.navigation.Navigation
import com.holparb.moviefinder.movies.presentation.viewmodels.MainViewModel

@Composable
fun MainScreen() {
    val viewModel = hiltViewModel<MainViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    var bottomNavVisible by rememberSaveable {
        mutableStateOf(true)
    }
    navController.currentBackStackEntryAsState().value?.destination?.let { currentDestination ->
        bottomNavVisible = !currentDestination.hasRoute(BottomNavScreenComposable::class)
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                selectedIndex = state.selectedIndex,
                changeNavigationSelectedIndex = viewModel::changeNavigationSelectedIndex
            )
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            Navigation(navController = navController)
        }
    }

}
