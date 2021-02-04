package ru.d3st.academyandroid.database

import androidx.room.*

@Dao
interface MovieWitActorsDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertActors(join: MovieActorCrossRef)

    @Transaction
    @Query("SELECT * FROM DatabaseMovie")
    fun getAllActorsTheMovie(): List<MovieWithActors>
}