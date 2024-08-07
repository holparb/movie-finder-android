package com.holparb.moviefinder.movies.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.holparb.moviefinder.movies.presentation.screens.HomeScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation(
    navController: NavHostController,
) {
    NavHost(navController = navController, startDestination = HomeScreenComposable) {
        composable<HomeScreenComposable> {
            HomeScreen()
        }
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController
) : T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}

@Serializable
object HomeScreenComposable