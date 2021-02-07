package ru.d3st.academyandroid.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieWithActorsDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActors(actors: List<DatabaseActor>)

    @Transaction
    @Query("SELECT * FROM databasemovie where movieId= :movieIdKey")
    fun getActorsTheMovie(movieIdKey: Int): MovieWithActors


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieWithActors(
        actors: List<DatabaseActor>,
        movieWithActors: List<MovieActorCrossRef>
    )
}

