package com.holparb.moviefinder.movies.presentation.components.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.holparb.moviefinder.movies.presentation.events.MovieListLoadEvent
import com.holparb.moviefinder.movies.presentation.screens.HomeScreen
import com.holparb.moviefinder.movies.presentation.screens.SeeMoreScreen
import com.holparb.moviefinder.movies.presentation.viewmodels.MovieListViewModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavScreenComposable.HomeScreenComposable) {
        composable<BottomNavScreenComposable.HomeScreenComposable> {
            HomeScreen(navController = navController)
        }
        composable<SeeMoreScreenComposable>(
            typeMap = mapOf(typeOf<MovieListLoadEvent>() to MovieListLoadEventType)
        ) {
            val args = it.toRoute<SeeMoreScreenComposable>()
            val movieListViewModel = hiltViewModel<MovieListViewModel>()
            val movieListState by movieListViewModel.state.collectAsStateWithLifecycle()
            SeeMoreScreen(state = movieListState, onEvent = movieListViewModel::onEvent, title = args.title, loadEvent = args.loadEvent, onBack = { navController.popBackStack() })
        }
    }
}

@Serializable
sealed class BottomNavScreenComposable {
    @Serializable
    object HomeScreenComposable
}

@Serializable
data class SeeMoreScreenComposable(
    val title: String,
    val loadEvent: MovieListLoadEvent
)
