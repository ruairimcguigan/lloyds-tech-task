package co.aquidigital.features.movies.domain.interactors

import co.aquidigital.features.movies.data.models.MoviesWithGenresResponse
import co.aquidigital.features.movies.domain.repository.MoviesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException

private const val RETRY_TIME_IN_MILLIS = 5000L

fun interface GetSimilarMoviesInteractor : (Int)  -> Flow<Result<MoviesWithGenresResponse>>

fun getSimilarMovies(
    movieId: Int,
    repository: MoviesRepository
): Flow<Result<MoviesWithGenresResponse>> =
    repository
        .getSimilarMovies(movieId = movieId)
        .map {
            Result.success(it)
        }
        .retryWhen { cause, _ ->
            if (cause is IOException) {
                emit(Result.failure(cause))
                delay(RETRY_TIME_IN_MILLIS)
                true
            } else {
                false
            }
        }
        .catch {
            emit(Result.failure(it))
        }