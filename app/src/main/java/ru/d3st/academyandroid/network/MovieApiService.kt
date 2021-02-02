package ru.d3st.academyandroid.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.d3st.academyandroid.domain.Genre
import ru.d3st.academyandroid.network.tmdb.*


private const val BASE_URL =
    "https://api.themoviedb.org/3/"
const val TMDB_API_KEY = "80281b9a87a6233e594ba72df3552657"

//Creating Auth Interceptor to add api_key query in front of all the requests.
private val authInterceptor = Interceptor { chain ->
    val newUrl = chain.request().url()
        .newBuilder()
        .addQueryParameter("api_key", TMDB_API_KEY)
        .build()
    val newRequest = chain.request()
        .newBuilder()
        .url(newUrl)
        .build()
    chain.proceed(newRequest)
}

//OkhttpClient for building http request url
private val tmdbClient = OkHttpClient().newBuilder()
    .addInterceptor(authInterceptor)
    .build()

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
    .client(tmdbClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


//val adapterMovieMoshi: JsonAdapter<ResponseMovieTMDB> = moshi.adapter(ResponseMovieTMDB::class.java)

interface MovieApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = TMDB_API_KEY,
        @Query("page") page: Int = 1
    ): ResponseMovieContainer

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = TMDB_API_KEY,
        @Query("page") page: Int = 1
    ): ResponseMovieContainer

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = TMDB_API_KEY,
        @Query("page") page: Int = 1

    ): ResponseMovieContainer

    //запрашиваем список фильмов сейчас в кино
    @GET("movie/now_playing")
    suspend fun getNovPlayingMovie(
        //@Query("api_key") apiKey: String = TMDB_API_KEY,
        @Query("page") page: Int = 1
    ): ResponseMovieContainer

    //запрашиваем актерский и режисерский состав данного фильма
    @GET("movie/{movie_id}/credits")
    suspend fun getActors(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = TMDB_API_KEY
    ): ResponseActorsContainer

    //запрашиваем все имеющиеся Жанры
    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String = TMDB_API_KEY
    ): ResponseGenreContainer

    //запрашиваем детальную биографию актера
    @GET("person/{person_id}")
    suspend fun getActorBio(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = TMDB_API_KEY
    ): ResponseActorBioContainer

    //запрашиваем фильмы данного актера
    @GET("person/{person_id}/movie_credits")
    suspend fun getActorsMovies(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String = TMDB_API_KEY
    ): ResponseMovieActorsContainer


}

object MovieApi {


    val retrofitService: MovieApiService by lazy { retrofit.create(MovieApiService::class.java) }
}


