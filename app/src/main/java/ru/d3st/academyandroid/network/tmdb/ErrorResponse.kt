package ru.d3st.academyandroid.network.tmdb


import com.squareup.moshi.Json

data class ErrorResponse(

    @Json(name = "status_message")
    val errorMessage: String?,

    @Json(name = "status_code")
    val errorCode: Int?
)


