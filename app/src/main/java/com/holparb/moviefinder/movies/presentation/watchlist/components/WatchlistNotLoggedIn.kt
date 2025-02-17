package com.holparb.moviefinder.movies.presentation.watchlist.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.holparb.moviefinder.R

@Composable
fun WatchlistNotLoggedIn(
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.watchlist_not_logged_in_message),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 22.sp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        OutlinedButton (
            onClick = { navigateToLogin() },
            shape = RoundedCornerShape(25),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = "Log in to TMDB",
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary)
            )
        }
    }
}