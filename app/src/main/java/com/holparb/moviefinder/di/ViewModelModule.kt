package com.holparb.moviefinder.di

import com.holparb.moviefinder.core.domain.util.login_form_validator.DefaultLoginFormValidator
import com.holparb.moviefinder.core.domain.util.login_form_validator.LoginFormValidator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Binds
    @ViewModelScoped
    abstract fun provideLoginFormValidator(
        defaultLoginFormValidator: DefaultLoginFormValidator
    ): LoginFormValidator
}