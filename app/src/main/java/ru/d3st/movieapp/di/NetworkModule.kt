package ru.d3st.movieapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.d3st.movieapp.network.MovieApi

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideMovieApi(): MovieApi {
        return MovieApi
    }
}