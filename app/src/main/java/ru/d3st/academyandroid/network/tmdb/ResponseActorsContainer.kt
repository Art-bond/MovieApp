package ru.d3st.academyandroid.network.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class ResponseActorsContainer(
    val id: Int, // 550
    val cast: List<Cast>,
){
    @JsonClass(generateAdapter = true)
    data class Cast(
        val adult: Boolean, // false
        val gender: Int, // 2
        val id: Int, // 819
        @Json(name = "known_for_department")
        val knownForDepartment: String, // Acting
        val name: String, // Edward Norton
        @Json(name = "original_name")
        val originalName: String, // Edward Norton
        val popularity: Double, // 7.861
        @Json(name = "profile_path")
        val profilePath: String?, // /5XBzD5WuTyVQZeS4VI25z2moMeY.jpg
        @Json(name = "cast_id")
        val castId: Int, // 4
        val character: String, // The Narrator
        @Json(name = "credit_id")
        val creditId: String, // 52fe4250c3a36847f80149f3
        val order: Int // 0
    )
}