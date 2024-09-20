package com.holparb.moviefinder.movies.presentation.components.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.holparb.moviefinder.movies.presentation.events.MovieListLoadEvent
import com.holparb.moviefinder.movies.presentation.screens.HomeScreen
import com.holparb.moviefinder.movies.presentation.screens.SeeMoreScreen
import com.holparb.moviefinder.movies.presentation.viewmodels.MovieListViewModel
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = HomeScreenComposable) {
        composable<HomeScreenComposable> {
            HomeScreen(navController = navController)
        }
        composable<SeeMoreScreenComposable>(
            typeMap = mapOf(typeOf<MovieListLoadEvent>() to MovieListLoadEventType)
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<SeeMoreScreenComposable>()
            SeeMoreScreen(
                title = args.title,
                loadEvent = args.loadEvent,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

@Serializable
object HomeScreenComposable

@Serializable
object WatchlistComposable

@Serializable
object SearchScreenComposable

@Serializable
data class SeeMoreScreenComposable(
    val title: String,
    val loadEvent: MovieListLoadEvent
)