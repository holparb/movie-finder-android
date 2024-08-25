package com.holparb.moviefinder.movies.presentation.components.vertical_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.movies.presentation.states.DataLoadState

@Composable
fun MovieVerticalList(
    dataLoadState: DataLoadState<List<MovieListItem>, MovieError>?,
    modifier: Modifier = Modifier
) {
    when(dataLoadState) {
        is DataLoadState.Error, null -> {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "Items failed to load, please check your network connection and try again",
                    style = TextStyle(
                        fontSize = 30.sp,
                        color = MaterialTheme.colorScheme.error
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        DataLoadState.Loading -> {
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
        is DataLoadState.Loaded -> {
            LazyColumn(
                modifier.fillMaxSize()
            ) {
                items(dataLoadState.data) { item ->
                    MovieVerticalListItem(
                        modifier = Modifier.height(250.dp),
                        movieListItem = item
                    )
                }
            }
        }
    }
}