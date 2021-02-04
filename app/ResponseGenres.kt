
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ResponseGenres(
    val genres: List<Genre>
) {
    @JsonClass(generateAdapter = true)
    data class Genre(
        val id: Int, // 28
        val name: String // Action
    )
}