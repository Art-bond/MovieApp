package ru.d3st.academyandroid.database

import androidx.room.*
import io.reactivex.Flowable
import io.reactivex.Observable

@Dao
interface MovieDao {

    @Query("select * from databasemovie")
    fun getMovies(): Observable<List<DatabaseMovie>>

    @Query("select * from databasemovie")
    fun getMoviesFlow(): Flowable<List<DatabaseMovie>>

    @Query("select * from databasemovie")
    fun getMoviesSync(): List<DatabaseMovie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(movies: List<DatabaseMovie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNowPlayingMovies(movies: List<DatabaseMovie>)

    @Update
    fun update(movie: DatabaseMovie)

    @Update
    fun updateAll(movies : List<DatabaseMovie>)

    @Query("select * from databasemovie where movieId = :movieIdKey")
    fun getMovie(movieIdKey: Int): Observable<DatabaseMovie>
}
