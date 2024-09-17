package com.holparb.moviefinder.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.holparb.moviefinder.movies.data.datasource.local.MovieDatabase
import com.holparb.moviefinder.movies.data.datasource.remote.MovieListType
import com.holparb.moviefinder.movies.data.datasource.remote.MovieRemoteMediator
import com.holparb.moviefinder.movies.data.datasource.remote.TmdbApi
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PagingModule {
    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    @Named("PopularMoviesPager")
    fun providePopularMoviesPager(movieDatabase: MovieDatabase, tmdbApi: TmdbApi): Pager<Int, MovieEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MovieRemoteMediator(
                movieDatabase = movieDatabase,
                tmdbApi = tmdbApi,
                movieListType = MovieListType.PopularMovies
            ),
            pagingSourceFactory = {movieDatabase.movieDao.getPopularMoviesWithPagination()}
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    @Named("TopRatedMoviesPager")
    fun provideTopRatedMoviesPager(movieDatabase: MovieDatabase, tmdbApi: TmdbApi): Pager<Int, MovieEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MovieRemoteMediator(
                movieDatabase = movieDatabase,
                tmdbApi = tmdbApi,
                movieListType = MovieListType.TopRatedMovies
            ),
            pagingSourceFactory = {movieDatabase.movieDao.getTopRatedMoviesWithPagination()}
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    @Named("UpcomingMoviesPager")
    fun provideUpcomingMoviesPager(movieDatabase: MovieDatabase, tmdbApi: TmdbApi): Pager<Int, MovieEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MovieRemoteMediator(
                movieDatabase = movieDatabase,
                tmdbApi = tmdbApi,
                movieListType = MovieListType.UpcomingMovies
            ),
            pagingSourceFactory = {movieDatabase.movieDao.getUpcomingMoviesWithPagination()}
        )
    }
}