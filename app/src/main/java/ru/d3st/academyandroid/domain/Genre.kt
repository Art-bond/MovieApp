package ru.d3st.academyandroid.domain

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
data class Genre(
    val id: Int, // 28
    val name: String // Action
    ) : Parcelable