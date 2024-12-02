package com.holparb.moviefinder.movies.presentation.details.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.holparb.moviefinder.R
import com.holparb.moviefinder.ui.theme.MovieFinderTheme

@Composable
fun MovieDetailsHeader(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.test_backdrop_path),
            contentDescription = "Movie backdrop path",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = 500.0f
                )
            )
        )
        Column (
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomStart)
        ) {
            Text(
                text = "The Lord of the Rings: The Return of the King" ,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "2003"
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Adventure"
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "3h 2m"
                )
            }
            Row {
                Icon(
                    imageVector = Icons.Default.Star,
                    tint = Color.Yellow,
                    contentDescription = "Star icon"
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "8.5" ,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieDetailsHeaderPreview() {
    MovieFinderTheme {
        MovieDetailsHeader(modifier = Modifier.height(400.dp))
    }
}