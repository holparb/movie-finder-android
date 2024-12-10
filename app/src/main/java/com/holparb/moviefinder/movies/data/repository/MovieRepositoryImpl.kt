package com.holparb.moviefinder.movies.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.holparb.moviefinder.core.domain.util.DatabaseError
import com.holparb.moviefinder.core.domain.util.Error
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.map
import com.holparb.moviefinder.movies.data.dao.MovieDao
import com.holparb.moviefinder.movies.data.datasource.remote.RemoteMoviesDataSource
import com.holparb.moviefinder.movies.data.dto.MovieListItemDto
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.data.mappers.toMovie
import com.holparb.moviefinder.movies.data.mappers.toMovieEntity
import com.holparb.moviefinder.movies.domain.model.Movie
import com.holparb.moviefinder.movies.domain.model.MovieListType
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class MovieRepositoryImpl @Inject constructor (
    private val moviesDataSource: RemoteMoviesDataSource,
    private val movieDao: MovieDao,
    @Named("PopularMoviesPager") private val popularMoviesPager: Pager<Int, MovieEntity>,
    @Named("TopRatedMoviesPager") private val topRatedMoviesPager: Pager<Int, MovieEntity>,
    @Named("UpcomingMoviesPager") private val upcomingMoviesPager: Pager<Int, MovieEntity>
): MovieRepository {
    private suspend fun saveMovieDtoToDatabase(movieListItemDto: MovieListItemDto, movieListType: MovieListType) {
        val entity = when(movieListType) {
            MovieListType.PopularMovies -> movieListItemDto.toMovieEntity(isPopular = true)
            MovieListType.TopRatedMovies -> movieListItemDto.toMovieEntity(isTopRated = true)
            MovieListType.UpcomingMovies -> movieListItemDto.toMovieEntity(isUpcoming = true)
        }
        movieDao.upsertMovie(entity)
    }

    private fun getMoviesWithPagination(pager: Pager<Int, MovieEntity>): Flow<PagingData<Movie>> {
        return pager.flow.map { pagingData ->
            pagingData.map {
                it.toMovie()
            }
        }
    }

    private suspend fun getMovies(
        movieListType: MovieListType,
        page: Int,
        region: String
    ): Result<List<Movie>, Error> {
        val movies = moviesDataSource.getMoviesList(
            movieListType = movieListType,
            page = page,
            region = region
        ).map {
            it.map { movieListItemDto ->
                try {
                    saveMovieDtoToDatabase(movieListItemDto = movieListItemDto, movieListType = movieListType)
                } catch(e: Exception) {
                    return Result.Error(DatabaseError.UPSERT_ERROR)
                }
                movieListItemDto.toMovie()
            }
        }
        return movies
    }

    override suspend fun getPopularMovies(
        page: Int,
        region: String
    ): Result<List<Movie>, Error> {
        return getMovies(
            movieListType = MovieListType.PopularMovies,
            page = page,
            region = region
        )
    }

    override suspend fun getPopularMoviesWithPagination(): Flow<PagingData<Movie>> {
        return getMoviesWithPagination(popularMoviesPager)
    }

    override suspend fun getTopRatedMoviesWithPagination(): Flow<PagingData<Movie>> {
        return getMoviesWithPagination(topRatedMoviesPager)
    }

    override suspend fun getUpcomingWithPagination(): Flow<PagingData<Movie>> {
        return getMoviesWithPagination(upcomingMoviesPager)
    }

    override suspend fun getMovieDetails(movieId: Int): Result<Movie, Error> {
        return moviesDataSource.getMovieDetails(movieId).map {
            it.toMovie()
        }
    }

    override suspend fun getTopRatedMovies(
        page: Int,
        region: String
    ): Result<List<Movie>, Error> {
        return getMovies(
            movieListType = MovieListType.TopRatedMovies,
            page = page,
            region = region
        )
    }

    override suspend fun getUpcomingMovies(
        page: Int,
        region: String
    ): Result<List<Movie>, Error> {
        return getMovies(
            movieListType = MovieListType.UpcomingMovies,
            page = page,
            region = region
        )
    }
}