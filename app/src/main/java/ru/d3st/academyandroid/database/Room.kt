package ru.d3st.academyandroid.database

import android.content.Context
import androidx.room.*


@Database(
    entities = [DatabaseMovie::class, DatabaseActor::class, MovieActorCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MoviesDataBase : RoomDatabase() {
    abstract val actorDao: MovieWitActorsDao
    abstract val movieDao: MovieDao
}

private lateinit var INSTANCE: MoviesDataBase

fun getDatabase(context: Context): MoviesDataBase {
    synchronized(MoviesDataBase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MoviesDataBase::class.java,
                "MovieDB"
            ).build()
        }
    }
    return INSTANCE
}