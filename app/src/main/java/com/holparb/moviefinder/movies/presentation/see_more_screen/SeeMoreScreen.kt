package com.holparb.moviefinder.movies.presentation.see_more_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.presentation.see_more_screen.components.MovieVerticalList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeeMoreScreen(
    title: String,
    listType: MovieListType,
    navigateToDetails: (Int) -> Unit,
    onBack: () -> Unit
) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    LaunchedEffect(Unit) {
        movieListViewModel.loadMovies(listType)
    }
    val movies = movieListViewModel.pagingDataFlow.collectAsLazyPagingItems()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                ),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = title,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            MovieVerticalList(movies = movies, navigateToDetails  = navigateToDetails)
        }
    }
}

@Preview
@Composable
private fun SeeMoreScreenPreview() {
    SeeMoreScreen(title = "Movies", listType = MovieListType.PopularMovies, navigateToDetails = {}, onBack = {})
}