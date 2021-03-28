package ru.d3st.movieapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import ru.d3st.movieapp.network.MovieApi

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideMovieApi(): MovieApi {
        return MovieApi
    }

    @Provides
    fun provideMainCoroutineScope(): CoroutineScope = CoroutineScope(Job() + Dispatchers.Main)

}