package com.holparb.moviefinder.di

import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.errors.DataError
import com.holparb.moviefinder.core.domain.util.pagination.MovieListPaginator
import com.holparb.moviefinder.movies.domain.model.Movie
import dagger.assisted.AssistedFactory

@AssistedFactory
interface MovieListPaginatorFactory {
    fun create(
        onLoadUpdated: (Boolean) -> Unit,
        onRequest: suspend (page: Int) -> Result<List<Movie>, DataError>,
        onError: suspend (DataError) -> Unit,
        onSuccess: suspend (movies: List<Movie>) -> Unit
    ): MovieListPaginator
}