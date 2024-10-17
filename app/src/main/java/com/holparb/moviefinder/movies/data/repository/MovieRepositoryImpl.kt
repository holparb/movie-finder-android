package com.holparb.moviefinder.movies.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.holparb.moviefinder.movies.data.dao.MovieDao
import com.holparb.moviefinder.movies.data.datasource.remote.MovieListType
import com.holparb.moviefinder.movies.data.datasource.remote.TmdbApi
import com.holparb.moviefinder.movies.data.dto.MovieListItemDto
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.data.mappers.toMovieEntity
import com.holparb.moviefinder.movies.data.mappers.toMovieListItem
import com.holparb.moviefinder.movies.domain.model.MovieListItem
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import com.holparb.moviefinder.movies.domain.util.MovieError
import com.holparb.moviefinder.movies.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class MovieRepositoryImpl @Inject constructor (
    private val api: TmdbApi,
    private val movieDao: MovieDao,
    @Named("PopularMoviesPager") private val popularMoviesPager: Pager<Int, MovieEntity>,
    @Named("TopRatedMoviesPager") private val topRatedMoviesPager: Pager<Int, MovieEntity>,
    @Named("UpcomingMoviesPager") private val upcomingMoviesPager: Pager<Int, MovieEntity>
): MovieRepository {

    private suspend fun getMovies(
        page: Int,
        region: String,
        movieListType: MovieListType,
    ): Resource<List<MovieListItem>, MovieError> {
       return try {
            val results = when(movieListType) {
                MovieListType.PopularMovies -> api.getPopularMovies(page = page, region = region).results
                MovieListType.TopRatedMovies -> api.getTopRatedMovies(page = page, region = region).results
                MovieListType.UpcomingMovies -> api.getUpcomingMovies(page = page, region = region).results
            }
            Resource.Success(
                results.map { movieListItemDto ->
                    try {
                        saveMovieDtoToDatabase(movieListItemDto, movieListType)
                    }
                    catch (e: Exception) {
                        Resource.Error(error = MovieError.LocalDatabaseError("There was an error during caching data"))
                    }
                    movieListItemDto.toMovieListItem()
                }
            )
        }
        catch(e: Exception) {
            Log.e(this::class.simpleName, e.localizedMessage ?: "Unknown error")
            Resource.Error(
                error = MovieError.NetworkError("Couldn't fetch movie data, please try again!")
            )
        }
    }

    private suspend fun saveMovieDtoToDatabase(movieListItemDto: MovieListItemDto, movieListType: MovieListType) {
        val entity = when(movieListType) {
            MovieListType.PopularMovies -> movieListItemDto.toMovieEntity(isPopular = true)
            MovieListType.TopRatedMovies -> movieListItemDto.toMovieEntity(isTopRated = true)
            MovieListType.UpcomingMovies -> movieListItemDto.toMovieEntity(isUpcoming = true)
        }
        movieDao.upsertMovie(entity)
    }

    private fun getMoviesWithPagination(pager: Pager<Int, MovieEntity>): Flow<PagingData<MovieListItem>> {
        return pager.flow.map { pagingData ->
            pagingData.map {
                it.toMovieListItem()
            }
        }
    }

    override suspend fun getPopularMovies(
        page: Int,
        region: String
    ): Resource<List<MovieListItem>, MovieError> {
        return getMovies(
            movieListType = MovieListType.PopularMovies,
            page = page,
            region = region
        )
    }

    override suspend fun getPopularMoviesWithPagination(): Flow<PagingData<MovieListItem>> {
        return getMoviesWithPagination(popularMoviesPager)
    }

    override suspend fun getTopRatedMoviesWithPagination(): Flow<PagingData<MovieListItem>> {
        return getMoviesWithPagination(topRatedMoviesPager)
    }

    override suspend fun getUpcomingWithPagination(): Flow<PagingData<MovieListItem>> {
        return getMoviesWithPagination(upcomingMoviesPager)
    }

    override suspend fun getTopRatedMovies(
        page: Int,
        region: String
    ): Resource<List<MovieListItem>, MovieError> {
        return getMovies(
            movieListType = MovieListType.TopRatedMovies,
            page = page,
            region = region
        )
    }

    override suspend fun getUpcomingMovies(
        page: Int,
        region: String
    ): Resource<List<MovieListItem>, MovieError> {
        return getMovies(
            movieListType = MovieListType.UpcomingMovies,
            page = page,
            region = region
        )
    }
}