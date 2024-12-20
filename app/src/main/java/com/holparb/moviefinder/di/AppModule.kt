package com.holparb.moviefinder.di

import android.content.Context
import androidx.room.Room
import com.holparb.moviefinder.auth.data.datasource.AuthDataSource
import com.holparb.moviefinder.auth.data.repository.AuthRepositoryImpl
import com.holparb.moviefinder.auth.domain.repository.AuthRepository
import com.holparb.moviefinder.core.data.local_storage.DefaultLocalEncryptedStorage
import com.holparb.moviefinder.core.data.networking.HttpClientFactory
import com.holparb.moviefinder.core.domain.util.LocalEncryptedStorage
import com.holparb.moviefinder.movies.data.dao.MovieDao
import com.holparb.moviefinder.movies.data.dao.RemoteKeyDao
import com.holparb.moviefinder.movies.data.datasource.local.MovieDatabase
import com.holparb.moviefinder.movies.data.datasource.remote.RemoteMoviesDataSource
import com.holparb.moviefinder.movies.data.repository.MovieRepositoryImpl
import com.holparb.moviefinder.movies.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClientFactory.create(CIO.create())
    }

    @Provides
    @Singleton
    fun provideRemoteMovieDatasource(httpClient: HttpClient): RemoteMoviesDataSource {
        return RemoteMoviesDataSource(httpClient)
    }

    @Provides
    @Singleton
    fun provideAuthDataSource(httpClient: HttpClient): AuthDataSource {
        return AuthDataSource(httpClient)
    }

    @Provides
    @Singleton
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao {
        return movieDatabase.movieDao
    }

    @Provides
    @Singleton
    fun provideRemoteKeyDao(movieDatabase: MovieDatabase): RemoteKeyDao {
        return movieDatabase.remoteKeyDao
    }

    @Provides
    @Singleton
    fun provideLocalDatabase(@ApplicationContext appContext: Context): MovieDatabase {
        return Room.databaseBuilder(
            appContext,
            MovieDatabase::class.java,
            name = "movie-database.db"
        ).build()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindLocalEncryptedStorage(
        localEncryptedStorage: DefaultLocalEncryptedStorage
    ): LocalEncryptedStorage
}