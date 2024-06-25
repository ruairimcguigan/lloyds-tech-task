package co.aquidigital.features.movies.domain.repository

import co.aquidigital.features.movies.data.models.MoviesWithGenresResponse
import co.aquidigital.features.movies.domain.model.TopMovieDomainModel
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getTopRatedMovies(): Flow<List<TopMovieDomainModel>>

    fun getMovieDetails(movieId: Int): Flow<TopMovieDomainModel>

    fun getSimilarMovies(movieId: Int): Flow<MoviesWithGenresResponse>

    suspend fun refreshTopRated()
}
