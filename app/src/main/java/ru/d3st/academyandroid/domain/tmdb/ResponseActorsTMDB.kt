package ru.d3st.academyandroid.domain.tmdb

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


data class ResponseActorsTMDB(
    val id: Int, // 550
    val cast: List<Cast>,
    val crew: List<Crew>
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
    @JsonClass(generateAdapter = true)
    data class Crew(
        val adult: Boolean, // false
        val gender: Int, // 2
        val id: Int, // 376
        @Json(name = "known_for_department")
        val knownForDepartment: String, // Production
        val name: String, // Arnon Milchan
        @Json(name = "original_name")
        val originalName: String, // Arnon Milchan
        val popularity: Double, // 1.702
        @Json(name = "profile_path")
        val profilePath: String?, // /b2hBExX4NnczNAnLuTBF4kmNhZm.jpg
        @Json(name = "credit_id")
        val creditId: String, // 55731b8192514111610027d7
        val department: String, // Production
        val job: String // Executive Producer
    )
}