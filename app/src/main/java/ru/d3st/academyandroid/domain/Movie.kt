package ru.d3st.academyandroid.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Movie(
    val titleMovie: String, //Название фильма
    val logoMain: Int, //Лого на экране детальной информации
    val logoPreview: Int, // Лого на экране списка фильмов
    val genre: String, // Жанр Фильма
    val rating: Int, //рейтинг фильма
    val reviewCount: String, // количество оценок
    val storyline: String, //описание фильма
    val timeMovie: String, //продолжительность фильма
    val ageRestrict: String, //возрастные ограничения
    val actors: List<Actor>
) : Parcelable {
    @Parcelize
    data class Actor(
        val nameActor: String, //ФИО актера
        val photoActor: Int // Фото актера
    ): Parcelable
}

