package com.holparb.moviefinder.movies.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.holparb.moviefinder.core.domain.util.Result
import com.holparb.moviefinder.core.domain.util.errors.DataError
import com.holparb.moviefinder.core.domain.util.errors.DatabaseError
import com.holparb.moviefinder.core.domain.util.map
import com.holparb.moviefinder.movies.data.datasource.local.MovieDatabase
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
    private val movieDatabase: MovieDatabase,
    @Named("PopularMoviesPager") private val popularMoviesPager: Pager<Int, MovieEntity>,
    @Named("TopRatedMoviesPager") private val topRatedMoviesPager: Pager<Int, MovieEntity>,
    @Named("UpcomingMoviesPager") private val upcomingMoviesPager: Pager<Int, MovieEntity>
): MovieRepository {

    private fun getMoviesWithPagination(pager: Pager<Int, MovieEntity>): Flow<PagingData<Movie>> {
        return pager.flow.map { pagingData ->
            pagingData.map {
                it.toMovie()
            }
        }
    }

    private suspend fun getMoviesFromRemoteDataSource(
        movieListType: MovieListType,
        page: Int,
        region: String
    ): Result<List<Movie>, DataError> {
        val movieEntities: MutableList<MovieEntity> = mutableListOf()
        val result = moviesDataSource.getMoviesList(
            movieListType = movieListType,
            page = page,
            region = region
        ).map {
            it.map { movieListItemDto ->
                val movieEntity = movieDatabase.movieDao.getMovieById(movieListItemDto.id) ?: movieListItemDto.toMovieEntity()
                val movieEntityToAdd = when(movieListType) {
                    MovieListType.PopularMovies -> movieEntity.copy(isPopular = true)
                    MovieListType.TopRatedMovies -> movieEntity.copy(isTopRated = true)
                    MovieListType.UpcomingMovies -> movieEntity.copy(isUpcoming = true)
                }
                movieEntities.add(movieEntityToAdd)
                movieListItemDto.toMovie()
            }
        }
        movieDatabase.withTransaction {
            try {
                movieDatabase.movieDao.upsertMovies(movieEntities)
            } catch(e: Exception) {
                Log.e(this.javaClass.simpleName, e.toString())
            }
        }
        return when(result) {
            is Result.Error -> Result.Error(DataError.Network(result.error))
            is Result.Success -> Result.Success(result.data)
        }
    }

    override suspend fun getPopularMovies(
        page: Int,
        region: String
    ): Result<List<Movie>, DataError> {
        return getMoviesFromRemoteDataSource(
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

    override suspend fun getMovieDetails(movieId: Int): Result<Movie, DataError> {
        val movieDetailsEntity = movieDatabase.movieDao.getMovieById(movieId)!!
        if(movieDetailsEntity.details) {
            return Result.Success(movieDetailsEntity.toMovie())
        }

        return when(val result = moviesDataSource.getMovieDetails(movieId)) {
            is Result.Error -> Result.Error(DataError.Network(result.error))
            is Result.Success -> {
                val updatedEntity = result.data.toMovieEntity(
                    isPopular = movieDetailsEntity.isPopular,
                    isUpcoming = movieDetailsEntity.isUpcoming,
                    isTopRated = movieDetailsEntity.isTopRated,
                    isWatchlist = movieDetailsEntity.isWatchlist
                )
                try {
                    movieDatabase.movieDao.upsertMovie(updatedEntity)
                } catch (e: Exception) {
                    Log.e(this.javaClass.simpleName, e.toString())
                }

                Result.Success(updatedEntity.toMovie())
            }
        }
    }

    private suspend fun getWatchlistFromDatabase(page: Int): Result<List<Movie>, DataError.Database> {
        return try {
            val movies = movieDatabase.movieDao.getWatchlist((page - 1) * 20).map {
                it.toMovie()
            }
            Result.Success(movies)
        } catch (e: Exception) {
            Log.e(this.javaClass.simpleName, e.toString())
            Result.Error(DataError.Database(DatabaseError.FETCH_ERROR))
        }
    }

    private suspend fun writeWatchlistMovieDtosToDatabase(movieListItemDtos: List<MovieListItemDto>) {
        val movieEntities: MutableList<MovieEntity> = mutableListOf()
        movieListItemDtos.forEach { movieListItemDto ->
            val movieEntity = movieDatabase.movieDao.getMovieById(movieListItemDto.id) ?: movieListItemDto.toMovieEntity()
            movieEntities.add(movieEntity.copy(isWatchlist = true))
        }
        movieDatabase.withTransaction {
            try {
                movieDatabase.movieDao.upsertMovies(movieEntities)
            } catch(e: Exception) {
                Log.e(this.javaClass.simpleName, e.toString())
            }
        }
    }

    override suspend fun getWatchlist(
        sessionId: String,
        page: Int
    ): Result<List<Movie>, DataError> {
        when(val result = getWatchlistFromDatabase(page)) {
            is Result.Error -> return Result.Error(DataError.Database(result.error.databaseError))
            is Result.Success -> {
                if(result.data.isNotEmpty()) {
                    return Result.Success(result.data)
                } else {
                    when(val remoteResult = moviesDataSource.getWatchlist(sessionId = sessionId, page = page)) {
                        is Result.Error -> return Result.Error(DataError.Network(remoteResult.error))
                        is Result.Success -> {
                            writeWatchlistMovieDtosToDatabase(remoteResult.data)
                            return getWatchlistFromDatabase(page)
                        }
                    }
                }
            }
        }
    }

    override suspend fun getTopRatedMovies(
        page: Int,
        region: String
    ): Result<List<Movie>, DataError> {
        return getMoviesFromRemoteDataSource(
            movieListType = MovieListType.TopRatedMovies,
            page = page,
            region = region
        )
    }

    override suspend fun getUpcomingMovies(
        page: Int,
        region: String
    ): Result<List<Movie>, DataError> {
        return getMoviesFromRemoteDataSource(
            movieListType = MovieListType.UpcomingMovies,
            page = page,
            region = region
        )
    }
}