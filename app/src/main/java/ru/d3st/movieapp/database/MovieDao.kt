package ru.d3st.movieapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("select * from databasemovie")
    fun getMovies(): LiveData<List<DatabaseMovie>>

    @Query("select * from databasemovie")
    fun getMoviesFlow(): Flow<List<DatabaseMovie>>

    @Query("select * from databasemovie")
    fun getMoviesSync(): List<DatabaseMovie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<DatabaseMovie>)

    @Update
    suspend fun update(movie: DatabaseMovie)

    @Update
    suspend fun updateAll(movies : List<DatabaseMovie>)

    @Query("select * from databasemovie where movieId = :movieIdKey")
    fun getMovie(movieIdKey: Int): DatabaseMovie
}
