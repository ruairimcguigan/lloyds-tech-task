package co.aquidigital.features.movies.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopMovieUi(
    val id: Int = 0,
    val title: String = "",
    val overview: String = "",
    val posterPath: String? = null,
    val backdropPath: String? = null,
    val voteAverage: Double? = null
): Parcelable

@Parcelize
data class SimilarMovieUiModel(
    val id: Int,
    val title: String,
    val poster_path: String?,
    val vote_average: Double,
    val genre_ids: List<Int>
): Parcelable