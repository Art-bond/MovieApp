package ru.d3st.academyandroid.domain.tmdb


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseActorsMovieTMDB(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int // 287
) {
    @JsonClass(generateAdapter = true)
    data class Cast(
        val character: String?, // Tristan Ludlow
        @Json(name = "credit_id")
        val creditId: String, // 52fe43c4c3a36847f806e20d
        @Json(name = "release_date")
        val releaseDate: String?, // 1994-12-16
        @Json(name = "vote_count")
        val voteCount: Int, // 568
        val video: Boolean, // false
        val adult: Boolean, // false
        @Json(name = "vote_average")
        val voteAverage: Double, // 7.2
        val title: String, // Legends of the Fall
        @Json(name = "genre_ids")
        val genreIds: List<Int>,
        @Json(name = "original_language")
        val originalLanguage: String, // en
        @Json(name = "original_title")
        val originalTitle: String, // Legends of the Fall
        val popularity: Double, // 2.356929
        val id: Int, // 4476
        @Json(name = "backdrop_path")
        val backdropPath: String?, // /jet7PQMY8aVzxBvkpG4P0eQI2n6.jpg
        val overview: String, // An epic tale of three brothers and their father living in the remote wilderness of 1900s USA and how their lives are affected by nature, history, war, and love.
        @Json(name = "poster_path")
        val posterPath: String? // /uh0sJcx3SLtclJSuKAXl6Tt6AV0.jpg
    )

    @JsonClass(generateAdapter = true)
    data class Crew(
        val id: Int, // 76203
        val department: String, // Production
        @Json(name = "original_language")
        val originalLanguage: String, // en
        @Json(name = "original_title")
        val originalTitle: String, // 12 Years a Slave
        val job: String, // Producer
        val overview: String, // In the pre-Civil War United States, Solomon Northup, a free black man from upstate New York, is abducted and sold into slavery. Facing cruelty as well as unexpected kindnesses Solomon struggles not only to stay alive, but to retain his dignity. In the twelfth year of his unforgettable odyssey, Solomon’s chance meeting with a Canadian abolitionist will forever alter his life.
        @Json(name = "vote_count")
        val voteCount: Int, // 3284
        val video: Boolean, // false
        @Json(name = "poster_path")
        val posterPath: String?, // /kb3X943WMIJYVg4SOAyK0pmWL5D.jpg
        @Json(name = "backdrop_path")
        val backdropPath: String?, // /xnRPoFI7wzOYviw3PmoG94X2Lnc.jpg
        val title: String, // 12 Years a Slave
        val popularity: Double, // 6.62674
        @Json(name = "genre_ids")
        val genreIds: List<Int>,
        @Json(name = "vote_average")
        val voteAverage: Double, // 7.9
        val adult: Boolean, // false
        @Json(name = "release_date")
        val releaseDate: String?, // 2013-10-18
        @Json(name = "credit_id")
        val creditId: String // 52fe492cc3a368484e11dfe1
    )
}