package ru.d3st.academyandroid.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Movie(
    val id: Int, //ID фильма
    val title: String, //название фильма
    val overview: String, //описание
    val poster: String?,
    val backdrop: String,
    val ratings: Float,
    val adult: Boolean,
    val runtime: Int,
    val genres: List<String>,
    val votes: Int
) : Parcelable

