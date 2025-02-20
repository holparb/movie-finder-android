package com.holparb.moviefinder.core.pagination

interface MovieListPaginator {
    suspend fun loadNextPage()
    fun reset()
}