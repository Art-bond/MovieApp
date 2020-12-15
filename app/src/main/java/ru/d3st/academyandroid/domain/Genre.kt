package ru.d3st.academyandroid.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre(
    val id: Int, // 28
    val name: String // Action
    ) : Parcelable