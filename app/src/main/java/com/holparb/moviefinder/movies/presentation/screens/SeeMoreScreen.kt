package com.holparb.moviefinder.movies.presentation.screens

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
import com.holparb.moviefinder.movies.presentation.components.vertical_list.MovieVerticalList
import com.holparb.moviefinder.movies.presentation.events.MovieListLoadEvent
import com.holparb.moviefinder.movies.presentation.viewmodels.MovieListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeeMoreScreen(
    title: String,
    loadEvent: MovieListLoadEvent,
    onBack: () -> Unit
) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movies = movieListViewModel.pagingDataFlow.collectAsLazyPagingItems()
    LaunchedEffect(Unit) {
        movieListViewModel.onEvent(loadEvent)
    }
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
                            contentDescription = "Localized description"
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
            MovieVerticalList(movies = movies)
        }
    }
}

@Preview
@Composable
private fun SeeMoreScreenPreview() {
    SeeMoreScreen(title = "Movies", loadEvent = MovieListLoadEvent.Unknown, onBack = {})
}