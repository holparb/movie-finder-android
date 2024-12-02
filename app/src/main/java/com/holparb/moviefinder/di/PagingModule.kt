package com.holparb.moviefinder.di

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.holparb.moviefinder.movies.data.datasource.local.MovieDatabase
import com.holparb.moviefinder.movies.data.datasource.remote.MovieRemoteMediator
import com.holparb.moviefinder.movies.data.datasource.remote.RemoteMoviesDataSource
import com.holparb.moviefinder.movies.data.entity.MovieEntity
import com.holparb.moviefinder.movies.domain.model.MovieListType
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
    fun providePopularMoviesPager(movieDatabase: MovieDatabase, remoteMoviesDataSource: RemoteMoviesDataSource): Pager<Int, MovieEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MovieRemoteMediator(
                movieDatabase = movieDatabase,
                remoteMoviesDataSource = remoteMoviesDataSource,
                movieListType = MovieListType.PopularMovies
            ),
            pagingSourceFactory = {movieDatabase.movieDao.getPopularMoviesWithPagination()}
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    @Named("TopRatedMoviesPager")
    fun provideTopRatedMoviesPager(movieDatabase: MovieDatabase, remoteMoviesDataSource: RemoteMoviesDataSource): Pager<Int, MovieEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MovieRemoteMediator(
                movieDatabase = movieDatabase,
                remoteMoviesDataSource = remoteMoviesDataSource,
                movieListType = MovieListType.TopRatedMovies
            ),
            pagingSourceFactory = {movieDatabase.movieDao.getTopRatedMoviesWithPagination()}
        )
    }

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    @Singleton
    @Named("UpcomingMoviesPager")
    fun provideUpcomingMoviesPager(movieDatabase: MovieDatabase, remoteMoviesDataSource: RemoteMoviesDataSource): Pager<Int, MovieEntity> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            remoteMediator = MovieRemoteMediator(
                movieDatabase = movieDatabase,
                remoteMoviesDataSource = remoteMoviesDataSource,
                movieListType = MovieListType.UpcomingMovies
            ),
            pagingSourceFactory = {movieDatabase.movieDao.getUpcomingMoviesWithPagination()}
        )
    }
}