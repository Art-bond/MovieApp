package ru.d3st.academyandroid.database

import androidx.room.TypeConverter


class Converters {
    @TypeConverter
    fun fromGenres(genresList: List<String>): String {
        return genresList.joinToString(", ")
    }

    @TypeConverter
    fun toGenres(genresOneString: String): List<String> {
        return genresOneString.split(", ")
    }

}