package com.holparb.moviefinder.movies.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.presentation.home_screen.HomeScreen
import com.holparb.moviefinder.movies.presentation.see_more.SeeMoreScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = SubGraph.MainScreens) {
        navigation<SubGraph.MainScreens>(
            startDestination = Destination.HomeScreenComposable
        ) {
            composable<Destination.HomeScreenComposable> {
                HomeScreen(navController = navController)
            }
        }
        navigation<SubGraph.SeeMoreScreens>(
            startDestination = Destination.SeeMoreScreenComposable()
        ) {
            composable<Destination.SeeMoreScreenComposable> { backStackEntry ->
                val args = backStackEntry.toRoute<Destination.SeeMoreScreenComposable>()
                SeeMoreScreen(
                    title = args.title,
                    listType = args.listType,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

sealed class SubGraph {
    @Serializable
    data object MainScreens: SubGraph()

    @Serializable
    data object SeeMoreScreens: SubGraph()
}

sealed class Destination {
    @Serializable
    data object HomeScreenComposable: Destination()

    @Serializable
    data object WatchlistComposable: Destination()

    @Serializable
    data object SearchScreenComposable: Destination()

    @Serializable
    data class SeeMoreScreenComposable(
        val title: String = "",
        val listType: MovieListType = MovieListType.PopularMovies
    ): Destination()
}
