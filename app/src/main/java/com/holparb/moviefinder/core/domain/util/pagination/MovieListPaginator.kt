package com.holparb.moviefinder.core.domain.util.pagination

import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.errors.DataError
import com.holparb.moviefinder.movies.domain.model.Movie
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@Suppress("INLINE_PROPERTY_WITH_BACKING_FIELD_DEPRECATION_WARNING")
class MovieListPaginator @AssistedInject constructor (
    @Assisted private inline val onLoadUpdated: (Boolean) -> Unit,
    @Assisted private inline val onRequest: suspend (page: Int) -> Result<List<Movie>, DataError>,
    @Assisted private inline val onError: suspend (DataError) -> Unit,
    @Assisted private inline val onSuccess: suspend (movies: List<Movie>) -> Unit
) {
    private var currentPage = 1
    private var isMakingRequest = false

    suspend fun loadNextPage() {
        if(isMakingRequest) return
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentPage)
        isMakingRequest = false
        when(result) {
            is Result.Error -> {
                onLoadUpdated(false)
                onError(result.error)
            }
            is Result.Success -> {
                currentPage ++
                onLoadUpdated(false)
                onSuccess(result.data)
            }
        }
    }

    fun reset() {
        currentPage = 1
    }
}