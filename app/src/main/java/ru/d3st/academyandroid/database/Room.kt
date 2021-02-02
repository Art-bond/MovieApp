package ru.d3st.academyandroid.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import ru.d3st.academyandroid.domain.Movie

@Dao
interface MovieDao{

    @Query("select * from databasemovie")
    fun getMovies(): LiveData<List<DatabaseMovie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movies : List<DatabaseMovie>)
}

@Database(entities = [DatabaseMovie::class],version = 1)
@TypeConverters(Converters::class)
abstract class MoviesDataBase: RoomDatabase(){
    abstract val movieDao : MovieDao
}

private lateinit var INSTANCE: MoviesDataBase

fun getDatabase(context: Context):MoviesDataBase{
    synchronized(MoviesDataBase::class.java){
        if (!::INSTANCE.isInitialized){
            INSTANCE = Room.databaseBuilder(context.applicationContext,
            MoviesDataBase::class.java,
            "movies").build()
        }
    }
    return INSTANCE
}