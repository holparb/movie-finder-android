package com.holparb.moviefinder.movies.presentation.watchlist

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.holparb.moviefinder.core.presentation.util.ObserveAsEvents

@Composable
fun WatchListScreen(
    navigateToLogin: () -> Unit
) {
    val watchlistViewModel = hiltViewModel<WatchlistViewModel>()
    ObserveAsEvents(
        events = watchlistViewModel.loggedInStatusChannel
    ) { isUserLoggedIn ->
        Log.d("WatchListScreen", "User logged in: $isUserLoggedIn")
    }
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier.fillMaxWidth().padding(paddingValues).fillMaxSize()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "WatchlistScreen"
            )
        }
    }
}