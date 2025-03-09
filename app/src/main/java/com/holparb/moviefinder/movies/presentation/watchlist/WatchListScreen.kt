package com.holparb.moviefinder.movies.presentation.watchlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.holparb.moviefinder.core.presentation.components.TopBar
import com.holparb.moviefinder.movies.presentation.watchlist.components.WatchlistBody

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchListScreen(
    navigateToDetails: (Int) -> Unit,
    navigateToLogin: () -> Unit
) {
    val watchlistViewModel = hiltViewModel<WatchlistViewModel>()
    val state by watchlistViewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        TopBar(
            title = {
                Text(
                    text = "Watchlist",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent,
            ),
            scrollBehavior = scrollBehavior
        )
        WatchlistBody(
            state = state,
            onAction = watchlistViewModel::onAction,
            events = watchlistViewModel.events,
            navigateToLogin = navigateToLogin,
            navigateToDetails = navigateToDetails
        )
    }
}