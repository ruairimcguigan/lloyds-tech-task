package co.aquidigital.features.movies.data.mapper

import co.aquidigital.features.movies.data.models.Movie
import co.aquidigital.features.movies.data.models.MovieDataModel
import co.aquidigital.features.movies.data.models.SimilarMovie
import co.aquidigital.features.movies.domain.model.SimilarMovieDomainModel
import co.aquidigital.features.movies.domain.model.TopMovieDomainModel
import co.aquidigital.features.movies.domain.model.TopMovieCachedModel

object DataMapper {

    fun TopMovieDomainModel.toEntityModel() = TopMovieCachedModel(
        id =  id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage
    )

    fun Movie.toDomainModel() = TopMovieDomainModel(
        id =  id,
        title = title,
        overview = overview,
        posterPath = poster_path,
        backdropPath = backdrop_path,
        voteAverage = vote_average
    )

    fun TopMovieCachedModel.toDomainModel() = TopMovieDomainModel(
        id =  id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage
    )

    fun MovieDataModel.toDomainModel() = TopMovieDomainModel(
        id =  id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage
    )

    fun SimilarMovie.toDomainModel() = SimilarMovieDomainModel(
        id = id,
        title = title,
        poster_path = poster_path,
        vote_average = vote_average,
        genre_ids = genre_ids
    )
}
