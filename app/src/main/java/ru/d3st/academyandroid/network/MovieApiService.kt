package ru.d3st.academyandroid.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.d3st.academyandroid.network.tmdb.*
import ru.d3st.academyandroid.utils.tmdbApiKey


private const val BASE_URL =
    "https://api.themoviedb.org/3/"


//Creating Auth Interceptor to add api_key query in front of all the requests.
private val authInterceptor = Interceptor { chain ->
    val newUrl = chain.request().url()
        .newBuilder()
        .addQueryParameter("api_key", tmdbApiKey)
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
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()


//val adapterMovieMoshi: JsonAdapter<ResponseMovieTMDB> = moshi.adapter(ResponseMovieTMDB::class.java)

interface MovieApiService {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int = 1
    ): ResponseMovieContainer

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1
    ): ResponseMovieContainer

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int = 1

    ): ResponseMovieContainer

    //запрашиваем список фильмов сейчас в кино
    @GET("movie/now_playing")
    fun getNovPlayingMovie(
       @Query("page") page: Int = 1
    ): Observable<ResponseMovieContainer>

    //запрашиваем актерский и режисерский состав данного фильма
    @GET("movie/{movie_id}/credits")
    fun getActors(
        @Path("movie_id") movieId: Int,
    ): Single<ResponseActorsContainer>

    //запрашиваем все имеющиеся Жанры
    @GET("genre/movie/list")
    fun getGenres(
    ): Single<ResponseGenreContainer>

    //запрашиваем детальную биографию актера
    @GET("person/{person_id}")
    fun getActorBio(
        @Path("person_id") personId: Int,
    ): Single<ResponseActorBioContainer>

    //запрашиваем фильмы данного актера
    @GET("person/{person_id}/movie_credits")
    fun getActorsMovies(
        @Path("person_id") personId: Int,
    ): Single<ResponseMovieActorsContainer>


}

object MovieApi {

    val networkService: MovieApiService by lazy { retrofit.create(MovieApiService::class.java) }
}


