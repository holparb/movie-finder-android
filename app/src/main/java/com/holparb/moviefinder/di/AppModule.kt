package com.holparb.moviefinder.di

import com.holparb.moviefinder.movies.data.datasource.remote.TmdbApi
import com.holparb.moviefinder.movies.data.repository.MovieRepositoryImpl
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesTmdbApi(): TmdbApi {
        return Retrofit.Builder()
            .baseUrl(TmdbApi.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class MovieRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository
}