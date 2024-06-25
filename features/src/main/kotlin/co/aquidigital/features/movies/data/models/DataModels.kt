package co.aquidigital.features.movies.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

data class MovieListResponse(val movies: List<MovieDataModel>)

@Serializable
data class MovieDataModel(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("vote_average") val voteAverage: Double?
)

@Serializable
data class TopRatedMoviesResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)

@Serializable
data class Movie(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
)

@Serializable
data class Genre(val id: Int = 0, val name: String)

@Serializable
data class GenreResponse(var genres: List<Genre>)

@Serializable
@Parcelize
data class MovieWithGenre(
    val id: Long,
    val title: String,
    val posterPath: String?,
    val voteAverage: Double,
    val genreNames: List<String?>
): Parcelable

@Serializable
data class MoviesWithGenresResponse(
    val movies: List<MovieWithGenre>
)

@Serializable
data class SimilarMovie(
    val id: Int,
    val title: String,
    val poster_path: String?,
    val vote_average: Double,
    val genre_ids: List<Int>
)

@Serializable
data class SimilarMovieResponse(
    val results: List<SimilarMovie>
)