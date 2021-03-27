package ru.d3st.movieapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [DatabaseMovie::class, DatabaseActor::class, MovieActorCrossRef::class],
    version = 10,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MoviesDataBase : RoomDatabase() {
    abstract fun actorDao(): MovieWithActorsDao
    abstract fun movieDao(): MovieDao
}

/*
@Volatile
private lateinit var INSTANCE: MoviesDataBase

fun getDatabase(context: Context): MoviesDataBase {
    synchronized(MoviesDataBase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                MoviesDataBase::class.java,
                DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}
*/

