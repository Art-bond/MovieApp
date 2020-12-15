package ru.d3st.academyandroid.network

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.d3st.academyandroid.domain.Genre
import ru.d3st.academyandroid.domain.tmdb.ResponseActorBioTMDB
import ru.d3st.academyandroid.domain.tmdb.ResponseActorsMovieTMDB
import ru.d3st.academyandroid.domain.tmdb.ResponseActorsTMDB
import ru.d3st.academyandroid.domain.tmdb.ResponseMovieTMDB


private const val BASE_URL =
    "https://api.themoviedb.org/3/"
const val API_KEY = "80281b9a87a6233e594ba72df3552657"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


//val adapterMovieMoshi: JsonAdapter<ResponseMovieTMDB> = moshi.adapter(ResponseMovieTMDB::class.java)

interface MovieApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): ResponseMovieTMDB

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): ResponseMovieTMDB

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1

    ): ResponseMovieTMDB

    //запрашиваем список фильмов сейчас в кино
    @GET("movie/now_playing")
    suspend fun getNovPlayingMovie(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("page") page: Int = 1
    ): ResponseMovieTMDB

    //запрашиваем актерский и режисерский состав данного фильма
    @GET("movie/{movie_id}/credits")
    suspend fun getActors(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): ResponseActorsTMDB

    //запрашиваем все имеющиеся Жанры
    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String = API_KEY
    ): ResponseGenreTMDB

    //запрашиваем детальную биографию актера
    @GET("person/{person_id}")
    suspend fun getActorBio(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): ResponseActorBioTMDB

    //запрашиваем фильмы данного актера
    @GET("person/{person_id}/movie_credits")
    suspend fun getActorsMovies(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): ResponseActorsMovieTMDB



}

object MovieApi {
    val retrofitService: MovieApiService by lazy { retrofit.create(MovieApiService::class.java) }
}

data class ResponseGenreTMDB(val genres: List<Genre>)
