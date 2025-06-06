package com.holparb.moviefinder.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.holparb.moviefinder.auth.presentation.login_screen.LoginScreen
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.presentation.details.MovieDetailsScreen
import com.holparb.moviefinder.movies.presentation.home_screen.HomeScreen
import com.holparb.moviefinder.movies.presentation.search.SearchScreen
import com.holparb.moviefinder.movies.presentation.see_more_screen.SeeMoreScreen
import com.holparb.moviefinder.movies.presentation.watchlist.WatchListScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = SubGraph.MainScreens) {
        navigation<SubGraph.MainScreens>(
            startDestination = Destination.HomeScreenDestination
        ) {
            composable<Destination.HomeScreenDestination> {
                HomeScreen(
                    onNavigateToSeeMoreScreen = { title, movieListType ->
                        navController.navigate(
                            Destination.SeeMoreScreenDestination(
                                title = title,
                                listType = movieListType
                            )
                        )
                    },
                    onNavigateToMovieDetails = { movieId ->
                        navController.navigate(
                            Destination.MovieDetailsDestination(movieId = movieId)
                        )
                    }
                )
            }
            composable<Destination.WatchlistDestination> {
                WatchListScreen(
                    navigateToLogin = {
                        navController.navigate(Destination.LoginScreenDestination) {
                            popUpTo(Destination.WatchlistDestination) { inclusive = true }
                        }
                    },
                    navigateToDetails = { movieId ->
                        navController.navigate(
                            Destination.MovieDetailsDestination(movieId = movieId)
                        )
                    }
                )
            }
            composable<Destination.SearchScreenDestination> {
                SearchScreen()
            }
        }
        composable<Destination.SeeMoreScreenDestination> { backStackEntry ->
            val args = backStackEntry.toRoute<Destination.SeeMoreScreenDestination>()
            SeeMoreScreen(
                title = args.title,
                listType = args.listType,
                navigateToDetails = { movieId ->
                    navController.navigate(
                        Destination.MovieDetailsDestination(movieId = movieId)
                    )
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable<Destination.MovieDetailsDestination> {
            MovieDetailsScreen(onBack = { navController.popBackStack() })
        }
        composable<Destination.LoginScreenDestination> {
            LoginScreen(
                navigateToWatchlist = { navController.navigate(Destination.WatchlistDestination) }
            )
        }
    }
}

sealed class SubGraph {
    @Serializable
    data object MainScreens: SubGraph()
}

sealed class Destination {
    @Serializable
    data object HomeScreenDestination: Destination()

    @Serializable
    data object WatchlistDestination: Destination()

    @Serializable
    data object SearchScreenDestination: Destination()

    @Serializable
    data class SeeMoreScreenDestination(
        val title: String = "",
        val listType: MovieListType = MovieListType.PopularMovies
    ): Destination()

    @Serializable
    data class MovieDetailsDestination(val movieId: Int = 0): Destination()

    @Serializable
    data object LoginScreenDestination: Destination()
}
