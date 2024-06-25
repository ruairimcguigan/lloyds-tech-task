package co.aquidigital.features.movies.presentation.mapper

import co.aquidigital.features.movies.domain.model.SimilarMovieDomainModel
import co.aquidigital.features.movies.domain.model.TopMovieDomainModel
import co.aquidigital.features.movies.presentation.model.SimilarMovieUiModel
import co.aquidigital.features.movies.presentation.model.TopMovieUi

fun TopMovieDomainModel.toUiModel() = TopMovieUi(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage
)

fun SimilarMovieDomainModel.toUiModel() = SimilarMovieUiModel(
    id = id,
    title = title,
    poster_path = poster_path,
    vote_average =  vote_average,
    genre_ids = genre_ids
)

data class TopMovieDomainModel(
    val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String,
    val backdropPath: String,
    val voteAverage: Double
)

fun TopMovieUi.toDomainModel() = TopMovieDomainModel(
    id = id,
    title = title,
    overview = overview,
    posterPath = posterPath,
    backdropPath = backdropPath,
    voteAverage = voteAverage
)
