package com.holparb.moviefinder.movies.presentation.watchlist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.moviefinder.movies.presentation.watchlist.components.WatchlistBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchListScreen(
    navigateToLogin: () -> Unit
) {
    val watchlistViewModel = hiltViewModel<WatchlistViewModel>()
    val state by watchlistViewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                ),
                modifier = Modifier.fillMaxWidth(),
                title = {
                    Text(
                        text = "Watchlist",
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddingValues ->
        WatchlistBody(
            state = state,
            navigateToLogin = navigateToLogin,
            modifier = Modifier.padding(paddingValues)
        )
    }
}