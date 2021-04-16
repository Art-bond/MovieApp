package ru.d3st.movieapp.network.tmdb


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseMovieContainer(
    val page: Int, // 1
    @Json(name = "results")
    val movies: List<NetworkMovies>,
    @Json(name = "total_pages")
    val totalPages: Int, // 33
    @Json(name = "total_results")
    val totalResults: Int // 649
) {
    @JsonClass(generateAdapter = true)
    data class NetworkMovies(
        @Json(name = "poster_path")
        val posterPath: String, // /e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg
        val adult: Boolean, // false
        val overview: String, // From DC Comics comes the Suicide Squad, an antihero team of incarcerated supervillains who act as deniable assets for the United States government, undertaking high-risk black ops missions in exchange for commuted prison sentences.
        @Json(name = "release_date")
        val releaseDate: String, // 2016-08-03
        @Json(name = "genre_ids")
        val genreIds: List<Int>,
        val id: Int, // 297761
        @Json(name = "original_title")
        val originalTitle: String, // Suicide Squad
        @Json(name = "original_language")
        val originalLanguage: String, // en
        val title: String, // Suicide Squad
        @Json(name = "backdrop_path")
        val backdropPath: String?, // /ndlQ2Cuc3cjTL7lTynw6I4boP4S.jpg
        val popularity: Double, // 48.261451
        @Json(name = "vote_count")
        val voteCount: Int, // 1466
        val video: Boolean, // false
        @Json(name = "vote_average")
        val voteAverage: Double // 5.91
    )


}