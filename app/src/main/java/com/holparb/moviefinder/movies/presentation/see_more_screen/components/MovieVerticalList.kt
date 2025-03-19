package com.holparb.moviefinder.movies.presentation.see_more_screen.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.holparb.moviefinder.movies.domain.model.Movie

@Composable
fun MovieVerticalList(
    movies: LazyPagingItems<Movie>,
    navigateToDetails: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = movies.loadState) {
        if(movies.loadState.refresh is LoadState.Error) {
            Log.e("MovieVerticalList", "Error: " + (movies.loadState.refresh as LoadState.Error).error.message)
            Toast.makeText(
                context,
                "Error: " + (movies.loadState.refresh as LoadState.Error).error.message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    when(movies.loadState.refresh) {
        is LoadState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .width(64.dp)
                        .align(Alignment.Center),
                    color = MaterialTheme.colorScheme.onBackground,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }
        else -> {
            LazyColumn(
                modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(count = movies.itemCount, key =  movies.itemKey { it.id }) { index ->
                    movies[index]?.let { movie ->
                        MovieVerticalListItem(
                            modifier = Modifier.height(200.dp),
                            movieVerticalListItemUi = movie.toMovieVerticalListItemUi(),
                            navigateToDetails = navigateToDetails
                        )
                    }
                }
            }
        }
    }
}