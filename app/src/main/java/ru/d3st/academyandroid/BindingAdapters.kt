package ru.d3st.academyandroid

import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ru.d3st.academyandroid.domain.Genre
import ru.d3st.academyandroid.domain.Movie
import ru.d3st.academyandroid.domain.tmdb.ResponseActorsMovieTMDB


/*@BindingAdapter("android:src")
fun setImageUri(view: ImageView, imageUri: String?) {
    if (imageUri == null) {
        view.setImageURI(null)
    } else {
        view.setImageURI(Uri.parse(imageUri))
    }
}

@BindingAdapter("android:src")
fun setImageUri(view: ImageView, imageUri: Uri?) {
    view.setImageURI(imageUri)
}

@BindingAdapter("android:src")
fun setImageDrawable(view: ImageView, drawable: Drawable?) {
    view.setImageDrawable(drawable)
}

@BindingAdapter("android:src")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}*/

//для установки изображений
//так же в манифесте стоит включить интернет пермешин
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri =
            imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}

@BindingAdapter("imageUrlplus")
fun bindImagePlus(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imagePath = "https://image.tmdb.org/t/p/w342$imgUrl"
        val imgUri =
            imagePath.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}

/*
@BindingAdapter("setDiffMember")
fun TextView.setGroupDiffMemberToday(item: Movie) {
    val diffMemberString = resources.getString(R.string.followers) + item.membersDiff.toString()
    text = diffMemberString
}*/

@BindingAdapter("setAgePermission")
fun TextView.setAgePermission(item: Movie) {
    text = if (item.adult) {
        context.getString(R.string.sexteen_age_plus)
    } else {
        context.getString(R.string.thirteen_age_plus)
    }
}

@BindingAdapter("setMovieGenres")
fun TextView.setMovieGenresAllInOne(item: Movie) {
    val genres = item.genres.map(Genre::name).joinToString()
    text = genres
}

@BindingAdapter("setReview")
fun TextView.setReview(item: Movie) {
    text = item.overview
}

//конвертируем рейтинг из 10 значного в пятизначный
@BindingAdapter("convertRating")
fun RatingBar.convertRating(item: Movie) {
    val newRating = item.ratings / 2
    rating = newRating
}


@BindingAdapter("setRunTime")
fun TextView.setRunTime(item: Movie) {
    "${item.runtime} min".also { text = it }
}


@BindingAdapter("countReviews")
fun TextView.countReviews(item: Movie) {
    "${item.votes} REVIEWS".also { text = it }
}