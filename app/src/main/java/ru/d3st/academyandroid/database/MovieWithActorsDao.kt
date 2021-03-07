package ru.d3st.academyandroid.database

import androidx.room.*
import io.reactivex.Observable

@Dao
interface MovieWithActorsDao {

    @Transaction
    @Query("SELECT * FROM databasemovie where movieId= :movieIdKey")
    fun getActorsTheMovie(movieIdKey: Int): Observable <MovieWithActors>


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieWithActors(
        actors: List<DatabaseActor>,
        movieWithActors: List<MovieActorCrossRef>
    )
}

