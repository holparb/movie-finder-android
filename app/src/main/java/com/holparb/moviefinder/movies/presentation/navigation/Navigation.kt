package com.holparb.moviefinder.movies.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.presentation.home_screen.HomeScreen
import com.holparb.moviefinder.movies.presentation.see_more.SeeMoreScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = HomeScreenComposable) {
        composable<HomeScreenComposable> {
            HomeScreen(navController = navController)
        }
        composable<SeeMoreScreenComposable> { backStackEntry ->
            val args = backStackEntry.toRoute<SeeMoreScreenComposable>()
            SeeMoreScreen(
                title = args.title,
                listType = args.listType,
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
    val listType: MovieListType
)