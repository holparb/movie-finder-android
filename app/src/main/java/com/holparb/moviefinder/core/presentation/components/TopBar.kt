package com.holparb.moviefinder.core.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    Surface(
        modifier = modifier,
        color = colors.containerColor,
    ) {
        val height by scrollBehavior?.let {
            remember {
                derivedStateOf {
                    if (it.state.collapsedFraction > 0) {
                        (56 * (1 - it.state.collapsedFraction)).dp
                    } else {
                        56.dp
                    }
                }
            }
        } ?: remember { mutableStateOf(56.dp) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .height(height.value.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            navigationIcon()
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                title()
            }
        }
    }
}