package com.holparb.moviefinder.core.pagination

import com.holparb.moviefinder.core.domain.util.Error
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.errors.DataError
import com.holparb.moviefinder.movies.domain.model.Movie

class MovieListPaginatorImpl(
    private val initialPage: Int,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (page: Int) -> Result<List<Movie>, DataError>,
    private inline val onError: suspend (Error) -> Unit,
    private inline val onSuccess: suspend (movies: List<Movie>) -> Unit
): MovieListPaginator {
    private var currentPage = initialPage
    private var isMakingRequest = false

    override suspend fun loadNextPage() {
        if(isMakingRequest) return
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentPage)
        isMakingRequest = false
        when(result) {
            is Result.Error -> {
                onError(result.error)
                onLoadUpdated(false)
            }
            is Result.Success -> {
                currentPage ++
                onSuccess(result.data)
                onLoadUpdated(false)
            }
        }
    }

    override fun reset() {
        currentPage = initialPage
    }
}