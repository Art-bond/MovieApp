package ru.d3st.movieapp.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.d3st.movieapp.database.MovieDao
import ru.d3st.movieapp.database.MovieWithActorsDao
import ru.d3st.movieapp.database.MoviesDataBase
import ru.d3st.movieapp.utils.DATABASE_NAME
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext appContext: Context): MoviesDataBase {
        return Room.databaseBuilder(
            appContext,
            MoviesDataBase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }


    @Provides
    fun provideMovieDao(database: MoviesDataBase): MovieDao {
        return database.movieDao()
    }
    @Provides
    fun provideActorDao(database: MoviesDataBase): MovieWithActorsDao {
        return database.actorDao()
    }
}