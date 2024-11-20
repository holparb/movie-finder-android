package com.holparb.moviefinder.movies.presentation.home_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.holparb.moviefinder.movies.presentation.home_screen.components.MainMovieItem
import com.holparb.moviefinder.movies.presentation.home_screen.components.MovieHorizontalList

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val homeScreenViewModel = hiltViewModel<HomeScreenViewModel>()
    val scrollState = rememberScrollState()
    val state by homeScreenViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        homeScreenViewModel.loadPopularMoviesAndMainItem()
        homeScreenViewModel.loadTopRatedMovies()
        homeScreenViewModel.loadUpcomingMovies()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
    ) {
        MainMovieItem(
            dataLoadState = state.movieLists[HomeScreenState.MAIN_ITEM]?.movieList,
            modifier = Modifier.height(280.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        MovieHorizontalList(
            state = state.movieLists[HomeScreenState.POPULAR_MOVIES]!!,
            title = "Popular movies",
            navController = navController
        )
        Spacer(modifier = Modifier.height(16.dp))
        MovieHorizontalList(
            state = state.movieLists[HomeScreenState.TOP_RATED_MOVIES]!!,
            title = "Top rated movies",
            navController = navController
        )
        Spacer(modifier = Modifier.height(16.dp))
        MovieHorizontalList(
            state = state.movieLists[HomeScreenState.UPCOMING_MOVIES]!!,
            title = "Upcoming movies",
            navController = navController
        )
    }
}