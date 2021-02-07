package ru.d3st.academyandroid.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Query("select * from databasemovie")
    fun getMovies(): LiveData<List<DatabaseMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies: List<DatabaseMovie>)

    @Update
    suspend fun update(movie: DatabaseMovie)

    @Query("select * from databasemovie where movieId = :movieIdKey")
    fun getMovie(movieIdKey: Int): DatabaseMovie
}
