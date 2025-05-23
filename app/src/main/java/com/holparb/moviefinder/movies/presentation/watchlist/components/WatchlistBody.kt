package com.holparb.moviefinder.movies.presentation.watchlist.components

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.holparb.moviefinder.core.domain.util.errors.DataError
import com.holparb.moviefinder.core.presentation.util.ObserveAsEvents
import com.holparb.moviefinder.core.presentation.util.toString
import com.holparb.moviefinder.movies.presentation.watchlist.WatchlistAction
import com.holparb.moviefinder.movies.presentation.watchlist.WatchlistEvent
import com.holparb.moviefinder.movies.presentation.watchlist.WatchlistState
import kotlinx.coroutines.flow.Flow

@Composable
fun WatchlistBody(
    state: WatchlistState,
    onAction: (WatchlistAction) -> Unit,
    events: Flow<WatchlistEvent>,
    navigateToLogin: () -> Unit,
    navigateToDetails: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    ObserveAsEvents(events = events) { event ->
        when(event) {
            is WatchlistEvent.WatchlistError -> {
                val toastText = when(event.error) {
                    is DataError.Database -> event.error.databaseError.toString(context)
                    is DataError.Network -> event.error.networkError.toString(context)
                }
                Toast.makeText(
                    context,
                    toastText,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    if(state.isUserLoggedIn.not()) {
        WatchlistNotLoggedIn(navigateToLogin = navigateToLogin)
    } else {
        Watchlist(
            state = state,
            navigateToDetails = navigateToDetails,
            onAction = onAction
        )
    }
}