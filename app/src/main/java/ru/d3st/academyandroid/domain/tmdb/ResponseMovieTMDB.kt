package ru.d3st.academyandroid.domain.tmdb


import com.squareup.moshi.Json

data class ResponseMovieTMDB(
    val page: Int, // 1
    val results: List<MovieNetwork>,
    @Json(name = "total_pages")
    val totalPages: Int, // 500
    @Json(name = "total_results")
    val totalResults: Int // 10000
) {
    data class MovieNetwork(
        val adult: Boolean, // false
        @Json(name = "backdrop_path")
        val backdropPath: String, // /jeAQdDX9nguP6YOX6QSWKDPkbBo.jpg
        @Json(name = "genre_ids")
        val genreIds: List<Int>,
        val id: Int, // 590706
        @Json(name = "original_language")
        val originalLanguage: String, // en
        @Json(name = "original_title")
        val originalTitle: String, // Jiu Jitsu
        val overview: String, // Every six years, an ancient order of jiu-jitsu fighters joins forces to battle a vicious race of alien invaders. But when a celebrated war hero goes down in defeat, the fate of the planet and mankind hangs in the balance.
        val popularity: Double, // 2420.373
        @Json(name = "poster_path")
        val posterPath: String?, // /eLT8Cu357VOwBVTitkmlDEg32Fs.jpg
        @Json(name = "release_date")
        val releaseDate: String, // 2020-11-20
        val title: String, // Jiu Jitsu
        val video: Boolean, // false
        @Json(name = "vote_average")
        val voteAverage: Double, // 5.9
        @Json(name = "vote_count")
        val voteCount: Int // 138
    )
}