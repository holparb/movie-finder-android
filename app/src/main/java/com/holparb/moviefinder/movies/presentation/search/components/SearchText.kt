package com.holparb.moviefinder.movies.presentation.search.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.holparb.moviefinder.movies.presentation.search.SearchAction
import com.holparb.moviefinder.ui.theme.MovieFinderTheme

@Composable
fun SearchText(
    text: String,
    onAction: (SearchAction.UpdateSearchText) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "search icon")
        },
        trailingIcon = {
            if(text.isNotBlank()) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "clear icon",
                    modifier = Modifier.clickable(
                        enabled = true,
                        onClick = {
                            println("Clear search")
                            onAction(SearchAction.UpdateSearchText(""))
                        }
                    )
                )
            }
        },
        value = text,
        placeholder = { Text("Search") },
        onValueChange = { onAction(SearchAction.UpdateSearchText(it)) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
            }
        ),
    )
}

@Preview(showBackground = true)
@Composable
private fun SearchTextPreview() {
    MovieFinderTheme {
        SearchText(text = "Search", onAction = {})
    }
}