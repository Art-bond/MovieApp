package ru.d3st.academyandroid.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import ru.d3st.academyandroid.network.*

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideActorManager(): ActorManager{
        return ActorManager()
    }
    @Provides
    fun provideMovieApi():MovieApi{
        return MovieApi
    }
}