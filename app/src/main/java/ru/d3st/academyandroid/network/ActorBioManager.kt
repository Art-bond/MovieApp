package ru.d3st.academyandroid.network

import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.d3st.academyandroid.network.Resource.*
import ru.d3st.academyandroid.network.tmdb.ErrorResponse
import ru.d3st.academyandroid.utils.Status

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): Resource<T> {
    return withContext(dispatcher) {
        try {
            InProgress
            Success(Status.SUCCESS, apiCall.invoke())
        } catch (throwable: Throwable) {
            Failure(Status.ERROR, throwable.message)
        }
    }

}

private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
    return try {
        throwable.response()?.errorBody()?.source()?.let {
            val moshiAdapter = Moshi.Builder().build().adapter(ErrorResponse::class.java)
            moshiAdapter.fromJson(it)
        }
    } catch (exception: Exception) {
        null
    }
}
